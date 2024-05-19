package com.example.ecommercemobile.store.domain.repository

import arrow.core.Either
import com.example.ecommercemobile.core.NetworkError
import com.example.ecommercemobile.store.domain.model.MetadataCart
import com.example.ecommercemobile.store.domain.model.Response
import com.example.ecommercemobile.store.domain.model.core.carts.AddCart
import com.example.ecommercemobile.store.domain.model.core.carts.DeleteCart

interface CartRepository {

    suspend fun getCart(headers: Map<String, String>): Either<NetworkError, Response<MetadataCart>>

    suspend fun addToCart(addCart: AddCart): Either<NetworkError, Response<MetadataCart>>

    suspend fun updateQuantityProduct(addCart: AddCart): Either<NetworkError, Response<MetadataCart>>

    suspend fun deleteProductFromCart(productId: Int): Either<NetworkError, Response<MetadataCart>>

    suspend fun deleteProductsFromCart(deleteCart: DeleteCart): Either<NetworkError, Response<MetadataCart>>

}