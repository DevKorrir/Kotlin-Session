package com.example.playground.ui.features.firestoredb.dataModel

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class FireProduct(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val category: String = "",
    val userId: String = "", // Owner of the product
    @ServerTimestamp
    val timestamp: Date? = null
)