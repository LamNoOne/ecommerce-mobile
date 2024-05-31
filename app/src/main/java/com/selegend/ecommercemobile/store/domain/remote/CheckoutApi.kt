package com.selegend.ecommercemobile.store.domain.remote

import com.selegend.ecommercemobile.store.domain.model.MetadataCheckout
import com.selegend.ecommercemobile.store.domain.model.MetadataOrders
import com.selegend.ecommercemobile.store.domain.model.Response
import com.selegend.ecommercemobile.store.domain.model.core.checkout.CheckoutFromCart
import retrofit2.http.*

interface CheckoutApi {
    @POST("checkout/order-from-cart")
    suspend fun checkoutFromCart(
        @HeaderMap headers: Map<String, String>,
        @Body checkout: CheckoutFromCart
    ): Response<MetadataCheckout>

    @GET("checkout/get-order")
    suspend fun getCheckoutOrder(
        @HeaderMap headers: Map<String, String>,
        @Query("orderId") orderId: Int
    ): Response<MetadataCheckout>

    @GET("checkout/get-all-orders")
    suspend fun getAllOrders(
        @HeaderMap headers: Map<String, String>,
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("sortBy") sortBy: String,
        @Query("order") order: String,
        @Query("status") status: String,
        @Query("name") name: String
    ): Response<MetadataOrders>
}