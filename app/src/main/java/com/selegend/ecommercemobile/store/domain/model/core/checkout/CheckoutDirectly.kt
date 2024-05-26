package com.selegend.ecommercemobile.store.domain.model.core.checkout

data class CheckoutDirectly(
    val userId: Int,
    val shipAddress: String,
    val phoneNumber: String,
    val paymentFormId: Int = 1,
    val orderProduct: List<OrderProduct>
)