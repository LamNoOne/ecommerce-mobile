package com.selegend.ecommercemobile.store.data.remote.repository

import arrow.core.Either
import com.selegend.ecommercemobile.core.NetworkError
import com.selegend.ecommercemobile.store.data.mapper.toNetworkError
import com.selegend.ecommercemobile.store.data.remote.ProductsApi
import com.selegend.ecommercemobile.store.domain.model.MetadataProduct
import com.selegend.ecommercemobile.store.domain.model.MetadataProductCategory
import com.selegend.ecommercemobile.store.domain.model.MetadataProducts
import com.selegend.ecommercemobile.store.domain.model.Response
import com.selegend.ecommercemobile.store.domain.repository.ProductsRepository
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val productsApi: ProductsApi
) : ProductsRepository {
    override suspend fun getProducts(
        name: String,
        categoryId: Int,
        page: Int,
        limit: Int,
        sortBy: String,
        order: String
    ): Either<NetworkError, Response<MetadataProducts>> {
        return Either.catch {
            productsApi.getProducts(name, categoryId, page, limit, sortBy, order)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun getProductById(id: Int): Either<NetworkError, Response<MetadataProduct>> {
        return Either.catch {
            productsApi.getProductById(id)
        }.mapLeft { it.toNetworkError() }
    }


    override suspend fun getProductsByCategory(
        id: Int,
        page: Int,
        limit: Int
    ): Either<NetworkError, Response<MetadataProductCategory>> {
        return Either.catch {
            productsApi.getProductsByCategory(id, page, limit)
        }.mapLeft { it.toNetworkError() }
    }
}