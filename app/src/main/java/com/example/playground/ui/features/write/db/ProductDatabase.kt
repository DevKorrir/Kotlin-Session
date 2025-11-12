package com.example.playground.ui.features.write.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.playground.ui.features.write.data.Product
import com.example.playground.ui.features.write.data.ProductDao

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    
    companion object {
        @Volatile
        private var INSTANCE: ProductDatabase? = null
        
        fun getDatabase(context: Context): ProductDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductDatabase::class.java,
                    "product_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}