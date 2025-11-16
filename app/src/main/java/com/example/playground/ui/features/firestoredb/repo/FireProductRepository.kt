package com.example.playground.ui.features.firestoredb.repo

import android.net.Uri
import com.example.playground.ui.features.firestoredb.dataModel.FireProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FireProductRepository private constructor() {
    
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()
    
    private val productsCollection = firestore.collection("products")
    
    // Get current user ID
    private fun getCurrentUserId(): String {
        return auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
    }
    
    // ============================================
    // READ Operations
    // ============================================
    
    // Get all products for current user (Real-time updates)
    fun getUserProducts(): Flow<List<FireProduct>> = callbackFlow {
        val userId = getCurrentUserId()
        
        val listener = productsCollection
            .whereEqualTo("userId", userId)
            //.orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val products = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(FireProduct::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                
                trySend(products)
            }
        
        awaitClose { listener.remove() }
    }
    
    // Get single product by ID
    suspend fun getProductById(productId: String): FireProduct? {
        return try {
            val document = productsCollection.document(productId).get().await()
            document.toObject(FireProduct::class.java)?.copy(id = document.id)
        } catch (e: Exception) {
            null
        }
    }
    
    // ============================================
    // CREATE Operation
    // ============================================
    
    suspend fun addProduct(product: FireProduct, imageUri: Uri?): Result<String> {
        return try {
            val userId = getCurrentUserId()
            
            // Upload image if exists
            val imageUrl = imageUri?.let { uploadImage(it) } ?: ""
            
            // Create product with user ID
            val productWithUser = product.copy(
                userId = userId,
                imageUrl = imageUrl
            )
            
            // Add to Firestore
            val docRef = productsCollection.add(productWithUser).await()
            
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // ============================================
    // UPDATE Operation
    // ============================================
    
    suspend fun updateProduct(productId: String, product: FireProduct, newImageUri: Uri?): Result<Unit> {
        return try {
            val userId = getCurrentUserId()
            
            // Check if product belongs to user
            val existingProduct = getProductById(productId)
            if (existingProduct?.userId != userId) {
                return Result.failure(SecurityException("Not authorized to update this product"))
            }
            
            // Upload new image if provided
            val imageUrl = if (newImageUri != null) {
                // Delete old image if exists
                if (existingProduct.imageUrl.isNotEmpty()) {
                    deleteImage(existingProduct.imageUrl)
                }
                uploadImage(newImageUri)
            } else {
                existingProduct.imageUrl
            }
            
            // Update product
            val updatedProduct = product.copy(
                id = productId,
                userId = userId,
                imageUrl = imageUrl
            )
            
            productsCollection.document(productId).set(updatedProduct).await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // ============================================
    // DELETE Operation
    // ============================================
    
    suspend fun deleteProduct(productId: String): Result<Unit> {
        return try {
            val userId = getCurrentUserId()
            
            // Get product to check ownership and delete image
            val product = getProductById(productId)
            if (product?.userId != userId) {
                return Result.failure(SecurityException("Not authorized to delete this product"))
            }
            
            // Delete image from storage
            if (product.imageUrl.isNotEmpty()) {
                deleteImage(product.imageUrl)
            }
            
            // Delete document
            productsCollection.document(productId).delete().await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // ============================================
    // IMAGE OPERATIONS (Firebase Storage)
    // ============================================
    
    private suspend fun uploadImage(imageUri: Uri): String {
        val userId = getCurrentUserId()
        val filename = "${System.currentTimeMillis()}.jpg"
        val storageRef = storage.reference
            .child("products/$userId/$filename")
        
        storageRef.putFile(imageUri).await()
        return storageRef.downloadUrl.await().toString()
    }
    
    private suspend fun deleteImage(imageUrl: String) {
        try {
            val storageRef = storage.getReferenceFromUrl(imageUrl)
            storageRef.delete().await()
        } catch (e: Exception) {
            // Image might not exist, ignore error
        }
    }
    
    // Singleton instance
    companion object {
        @Volatile
        private var INSTANCE: FireProductRepository? = null
        
        fun getInstance(): FireProductRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = FireProductRepository()
                INSTANCE = instance
                instance
            }
        }
    }
}