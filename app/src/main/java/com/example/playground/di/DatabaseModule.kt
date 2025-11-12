package com.example.playground.di

import android.content.Context
import com.example.playground.ui.features.write.data.ProductDao
import com.example.playground.ui.features.write.db.ProductDatabase
import com.example.playground.ui.features.write.repo.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ProductDatabase {
        return ProductDatabase.getDatabase(context)
    }
    
    @Provides
    fun provideProductDao(database: ProductDatabase): ProductDao {
        return database.productDao()
    }
    
    @Provides
    @Singleton
    fun provideRepository(productDao: ProductDao): ProductRepository {
        return ProductRepository(productDao)
    }
}