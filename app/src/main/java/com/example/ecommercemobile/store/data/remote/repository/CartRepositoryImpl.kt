package com.example.ecommercemobile.store.data.remote.repository

import arrow.core.Either
import com.example.ecommercemobile.core.NetworkError
import com.example.ecommercemobile.store.data.mapper.toNetworkError
import com.example.ecommercemobile.store.data.remote.CartApi
import com.example.ecommercemobile.store.domain.model.MetadataCart
import com.example.ecommercemobile.store.domain.model.Response
import com.example.ecommercemobile.store.domain.model.core.carts.AddCart
import com.example.ecommercemobile.store.domain.model.core.carts.DeleteCart
import com.example.ecommercemobile.store.domain.repository.CartRepository
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartApi: CartApi
): CartRepository {
    override suspend fun getCart(headers: Map<String, String>): Either<NetworkError, Response<MetadataCart>> {
        return Either.catch {
            cartApi.getCart(headers)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun addToCart(addCart: AddCart): Either<NetworkError, Response<MetadataCart>> {
        return Either.catch {
            cartApi.addToCart(addCart)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun updateQuantityProduct(addCart: AddCart): Either<NetworkError, Response<MetadataCart>> {
        return Either.catch {
            cartApi.updateQuantityProduct(addCart)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteProductFromCart(productId: Int): Either<NetworkError, Response<MetadataCart>> {
        return Either.catch {
            cartApi.deleteProductFromCart(productId)
        }.mapLeft { it.toNetworkError() }
    }

    override suspend fun deleteProductsFromCart(deleteCart: DeleteCart): Either<NetworkError, Response<MetadataCart>> {
        return Either.catch {
            cartApi.deleteProductsFromCart(deleteCart)
        }.mapLeft { it.toNetworkError() }
    }
}