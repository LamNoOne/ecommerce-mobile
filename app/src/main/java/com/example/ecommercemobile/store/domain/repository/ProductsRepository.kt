package com.example.ecommercemobile.store.domain.repository

import arrow.core.Either
import com.example.ecommercemobile.core.NetworkError
import com.example.ecommercemobile.store.domain.model.MetadataProduct
import com.example.ecommercemobile.store.domain.model.MetadataProductCategory
import com.example.ecommercemobile.store.domain.model.MetadataProducts
import com.example.ecommercemobile.store.domain.model.Response
import com.example.ecommercemobile.store.domain.model.core.Product

interface ProductsRepository {
    suspend fun getProducts(name: String): Either<NetworkError, Response<MetadataProducts>>

    suspend fun getProductById(id: Int): Either<NetworkError, Response<MetadataProduct>>

    suspend fun getProductsByCategory(id: Int): Either<NetworkError, Response<MetadataProductCategory>>
}