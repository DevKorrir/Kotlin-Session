package com.example.playground.ui.features.write.repo

import com.example.playground.ui.features.write.data.Product
import com.example.playground.ui.features.write.data.ProductDao
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {
    
    //fun getAllProducts(): Flow<List<Product>> = productDao.getAllProducts()
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()


    suspend fun insert(product: Product) = productDao.insert(product)
    suspend fun update(product: Product) = productDao.update(product)
    suspend fun delete(product: Product) = productDao.delete(product)
    //suspend fun getProductById(id: Int): Product? = productDao.getProductById(id)
    suspend fun getProductById(id: Int): Product? {
        return productDao.getProductById(id)
    }


}