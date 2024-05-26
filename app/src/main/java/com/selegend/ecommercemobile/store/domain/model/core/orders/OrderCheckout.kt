package com.selegend.ecommercemobile.store.domain.model.core.orders

import com.selegend.ecommercemobile.store.domain.model.core.payment.ProductPayment

data class OrderCheckout(
    val orderId: Int,
    val transactionId: String ?= null,
    val name: String ?= null,
    val firstName: String ?= null,
    val lastName: String ?= null,
    val email: String ?= null,
    val phoneNumber: String,
    val shipAddress: String,
    val orderStatus: String,
    val orderProducts: List<ProductPayment>,
    val totalAmount: Int
)
