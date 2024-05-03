package com.example.ecommercemobile.di

import com.example.ecommercemobile.store.data.repository.CategoriesRepositoryImpl
import com.example.ecommercemobile.store.data.repository.ProductsRepositoryImpl
import com.example.ecommercemobile.store.domain.repository.CategoriesRepository
import com.example.ecommercemobile.store.domain.repository.ProductsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindProductsRepository(impl: ProductsRepositoryImpl): ProductsRepository

    @Binds
    @Singleton
    abstract fun bindCategoriesRepository(impl: CategoriesRepositoryImpl): CategoriesRepository
}