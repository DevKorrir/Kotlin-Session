package com.example.playground.ui.features.firestoredb.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playground.ui.features.firestoredb.dataModel.FireProduct
import com.example.playground.ui.features.firestoredb.repo.FireProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductListViewModel : ViewModel() {
    
    private val repository = FireProductRepository.getInstance()
    
    // Real-time product list
    val products: StateFlow<List<FireProduct>> = repository.getUserProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    // Loading and error states
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    // Delete product
    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.deleteProduct(productId)
            _isLoading.value = false
            
            result.onFailure { exception ->
                _error.value = exception.message
            }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
}