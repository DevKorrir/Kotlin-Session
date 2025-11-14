package com.example.playground.ui.features.write.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    // CREATE
    @Insert
    suspend fun insert(product: Product)
    
    // READ
    @Query("SELECT * FROM products ORDER BY timestamp DESC")
    fun getAllProducts(): Flow<List<Product>>
    
//    @Query("SELECT * FROM products WHERE id = :productId")
//    suspend fun getProductById(productId: Int): Product?

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: Int): Product?
    
    // UPDATE
    @Update
    suspend fun update(product: Product)
    
    // DELETE
    @Delete
    suspend fun delete(product: Product)
    
    @Query("DELETE FROM products")
    suspend fun deleteAll()
}