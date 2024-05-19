package com.example.ecommercemobile.store.data.remote.repository

import arrow.core.Either
import com.example.ecommercemobile.core.NetworkError
import com.example.ecommercemobile.store.data.mapper.toNetworkError
import com.example.ecommercemobile.store.data.remote.CartApi
import com.example.ecommercemobile.store.domain.model.MetadataCart
import com.example.ecommercemobile.store.domain.model.Response
import com.example.ecommercemobile.store.domain.model.core.carts.AddCart
import com.example.ecommercemobile.store.domain.model.core.carts.DeleteCart
import com.example.ecommercemobile.store.domain.model.core.carts.UpdateCart
import com.example.ecommercemobile.store.domain.repository.CartRepository
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartApi: CartApi
) : CartRepository {
    override suspend fun getCart(headers: Map<String, String>): Either<NetworkError, Response<MetadataCart>> {
        return Either.catch {
            cartApi.getCart(headers)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun addToCart(
        headers: Map<String, String>,
        addCart: AddCart
    ): Either<NetworkError, Response<MetadataCart>> {
        return Either.catch {
            cartApi.addToCart(headers, addCart)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateQuantityProduct(
        headers: Map<String, String>,
        updateCart: UpdateCart
    ): Either<NetworkError, Response<MetadataCart>> {
        return Either.catch {
            cartApi.updateQuantityProduct(headers, updateCart)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteProductFromCart(
        headers: Map<String, String>,
        productId: Int
    ): Either<NetworkError, Response<MetadataCart>> {
        return Either.catch {
            cartApi.deleteProductFromCart(headers, productId)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteProductsFromCart(
        headers: Map<String, String>,
        deleteCart: DeleteCart
    ): Either<NetworkError, Response<MetadataCart>> {
        return Either.catch {
            cartApi.deleteProductsFromCart(headers, deleteCart)
        }.mapLeft { it.toNetworkError() }
    }
}