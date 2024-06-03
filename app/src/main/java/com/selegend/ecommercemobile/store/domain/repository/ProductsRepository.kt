package com.selegend.ecommercemobile.store.domain.repository

import arrow.core.Either
import com.selegend.ecommercemobile.core.NetworkError
import com.selegend.ecommercemobile.store.domain.model.MetadataProduct
import com.selegend.ecommercemobile.store.domain.model.MetadataProductCategory
import com.selegend.ecommercemobile.store.domain.model.MetadataProducts
import com.selegend.ecommercemobile.store.domain.model.Response

interface ProductsRepository {
    suspend fun getProducts(
        name: String,
        categoryId: String,
        page: Int,
        limit: Int,
        sortBy: String,
        order: String
    ): Either<NetworkError, Response<MetadataProducts>>

    suspend fun searchProduct(
        name: String,
        page: Int,
        limit: Int,
        sortBy: String,
        order: String
    ): Either<NetworkError, Response<MetadataProducts>>

    suspend fun getProductById(id: Int): Either<NetworkError, Response<MetadataProduct>>
    suspend fun getProductsByCategory(
        id: Int,
        page: Int,
        limit: Int
    ): Either<NetworkError, Response<MetadataProductCategory>>
}