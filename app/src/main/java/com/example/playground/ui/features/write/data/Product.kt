package com.example.playground.ui.features.write.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    val imageUri: String? = null, // stores image path
    val category: String,
    val timestamp: Long = System.currentTimeMillis()
)
