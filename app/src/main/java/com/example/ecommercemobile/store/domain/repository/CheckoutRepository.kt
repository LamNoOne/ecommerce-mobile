package com.example.ecommercemobile.store.domain.repository

import arrow.core.Either
import com.example.ecommercemobile.core.NetworkError
import com.example.ecommercemobile.store.domain.model.MetadataCheckout
import com.example.ecommercemobile.store.domain.model.MetadataOrders
import com.example.ecommercemobile.store.domain.model.Response
import com.example.ecommercemobile.store.domain.model.core.checkout.CheckoutFromCart

interface CheckoutRepository {
    suspend fun checkoutFromCart(
        headers: Map<String, String>,
        checkout: CheckoutFromCart
    ): Either<NetworkError, Response<MetadataCheckout>>

    suspend fun getCheckoutOrder(
        headers: Map<String, String>,
        orderId: Int
    ): Either<NetworkError, Response<MetadataCheckout>>

    suspend fun getAllOrders(
        headers: Map<String, String>,
        page: Int,
        limit: Int,
        name: Int
    ): Either<NetworkError, Response<MetadataOrders>>
}