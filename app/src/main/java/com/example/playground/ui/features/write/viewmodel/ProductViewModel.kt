package com.example.playground.ui.features.write.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playground.ui.features.write.data.Product
import com.example.playground.ui.features.write.repo.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ProductUiState {
    object Idle : ProductUiState
    object Loading : ProductUiState
    data class Error(val message: String) : ProductUiState
    object Success : ProductUiState
}

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _productUiState = MutableStateFlow<ProductUiState>(ProductUiState.Idle)
    val productUiState: StateFlow<ProductUiState> = _productUiState.asStateFlow()

    val allProducts: StateFlow<List<Product>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addProduct(name: String, description: String, price: Double,
                   imageUri: String?, category: String) {
        viewModelScope.launch {
            _productUiState.value = ProductUiState.Loading
            try {
                val product = Product(
                    name = name,
                    description = description,
                    price = price,
                    imageUri = imageUri,
                    category = category
                )
                repository.insert(product)
                _productUiState.value = ProductUiState.Success
            } catch (e: Exception) {
                _productUiState.value = ProductUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun resetState() {
        _productUiState.value = ProductUiState.Idle
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.update(product)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.delete(product)
        }
    }

    suspend fun getProductById(id: Int): Product? = repository.getProductById(id)
}
