package com.selegend.ecommercemobile.store.domain.model.core.checkout

data class CheckoutFromCart(
    val shipAddress: String,
    val phoneNumber: String,
    val paymentFormId: Int = 1,
    val orderProducts: List<Int>
)
