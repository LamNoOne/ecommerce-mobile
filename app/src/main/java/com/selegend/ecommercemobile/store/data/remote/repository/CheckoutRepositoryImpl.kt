package com.selegend.ecommercemobile.store.data.remote.repository

import arrow.core.Either
import com.selegend.ecommercemobile.core.NetworkError
import com.selegend.ecommercemobile.store.data.mapper.toNetworkError
import com.selegend.ecommercemobile.store.data.remote.CheckoutApi
import com.selegend.ecommercemobile.store.domain.model.MetadataCheckout
import com.selegend.ecommercemobile.store.domain.model.MetadataOrders
import com.selegend.ecommercemobile.store.domain.model.Response
import com.selegend.ecommercemobile.store.domain.model.core.checkout.CheckoutFromCart
import com.selegend.ecommercemobile.store.domain.repository.CheckoutRepository
import javax.inject.Inject

class CheckoutRepositoryImpl @Inject constructor(
    private val checkoutApi: CheckoutApi
) : CheckoutRepository {
    override suspend fun checkoutFromCart(
        headers: Map<String, String>,
        checkout: CheckoutFromCart
    ): Either<NetworkError, Response<MetadataCheckout>> {
        return Either.catch {
            checkoutApi.checkoutFromCart(headers, checkout)
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun getCheckoutOrder(
        headers: Map<String, String>,
        orderId: Int
    ): Either<NetworkError, Response<MetadataCheckout>> {
        return Either.catch {
            checkoutApi.getCheckoutOrder(headers, orderId)
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun getAllOrders(
        headers: Map<String, String>,
        page: Int,
        limit: Int,
        name: Int
    ): Either<NetworkError, Response<MetadataOrders>> {
        return Either.catch {
            checkoutApi.getAllOrders(headers, page, limit, name)
        }.mapLeft {
            it.toNetworkError()
        }
    }
}