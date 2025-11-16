package com.example.playground.ui.features.firestoredb.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playground.ui.features.firestoredb.dataModel.FireProduct
import com.example.playground.ui.features.firestoredb.repo.FireProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductWriteViewModel : ViewModel() {
    
    private val repository = FireProductRepository.getInstance()
    
    // Form states
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name
    
    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description
    
    private val _price = MutableStateFlow("")
    val price: StateFlow<String> = _price
    
    private val _category = MutableStateFlow("")
    val category: StateFlow<String> = _category
    
    private val _imageUri = MutableStateFlow<Uri?>(null)
    val imageUri: StateFlow<Uri?> = _imageUri
    
    // UI states
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved
    
    // Current product ID (for editing)
    private var currentProductId: String? = null
    
    // ============================================
    // Form Updates
    // ============================================
    
    fun updateName(value: String) { _name.value = value }
    fun updateDescription(value: String) { _description.value = value }
    fun updatePrice(value: String) { _price.value = value }
    fun updateCategory(value: String) { _category.value = value }
    fun updateImageUri(uri: Uri?) { _imageUri.value = uri }
    
    // ============================================
    // Load Product for Editing
    // ============================================
    
    fun loadProduct(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val product = repository.getProductById(productId)
            _isLoading.value = false
            
            product?.let {
                currentProductId = productId
                _name.value = it.name
                _description.value = it.description
                _price.value = it.price.toString()
                _category.value = it.category
                if (it.imageUrl.isNotEmpty()) {
                    // Store existing image URL (will be used if no new image selected)
                    _imageUri.value = Uri.parse(it.imageUrl)
                }
            }
        }
    }
    
    // ============================================
    // Save Product (Create or Update)
    // ============================================
    
    fun saveProduct() {
        // Validation
        if (_name.value.isBlank()) {
            _error.value = "Product name is required"
            return
        }
        
        val priceValue = _price.value.toDoubleOrNull()
        if (priceValue == null || priceValue <= 0) {
            _error.value = "Valid price is required"
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            
            val product = FireProduct(
                name = _name.value,
                description = _description.value,
                price = priceValue,
                category = _category.value
            )
            
            val result = if (currentProductId == null) {
                // Create new product
                repository.addProduct(product, _imageUri.value)
            } else {
                // Update existing product
                repository.updateProduct(currentProductId!!, product, _imageUri.value)
            }
            
            _isLoading.value = false
            
            result.onSuccess {
                _isSaved.value = true
            }.onFailure { exception ->
                _error.value = exception.message
            }
        }
    }
    
    // ============================================
    // Reset Form
    // ============================================
    
    fun resetForm() {
        currentProductId = null
        _name.value = ""
        _description.value = ""
        _price.value = ""
        _category.value = ""
        _imageUri.value = null
        _error.value = null
        _isSaved.value = false
    }
    
    fun clearError() {
        _error.value = null
    }
}