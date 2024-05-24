package com.example.ecommercemobile.store.domain.model.core.orders

data class OrderCheckout(
    val orderId: Int,
    val name: String? = null,
    val shipAddress: String,
    val phoneNumber: String,
    val orderStatus: String,
    val orderProducts: List<OrderProduct>,
    val totalAmount: Int
)
