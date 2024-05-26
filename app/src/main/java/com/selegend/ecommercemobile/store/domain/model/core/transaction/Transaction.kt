package com.selegend.ecommercemobile.store.domain.model.core.transaction

data class Transaction(
    val id: String,
    val status: String,
    val orderId: String
)
