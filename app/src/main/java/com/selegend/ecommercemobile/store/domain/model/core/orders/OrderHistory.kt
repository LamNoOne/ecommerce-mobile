package com.selegend.ecommercemobile.store.domain.model.core.orders


data class OrderHistory(
    val orderId: Int,
    val transactionId: String ?= null,
    val name: String,
    val firstName: String ?= null,
    val lastName: String ?= null,
    val email: String,
    val phoneNumber: String,
    val shipAddress: String,
    val orderStatus: String,
    val orderProducts: List<OrderProduct>,
    val totalAmount: Int,
    val createdAt: String,
    val updatedAt: String
)