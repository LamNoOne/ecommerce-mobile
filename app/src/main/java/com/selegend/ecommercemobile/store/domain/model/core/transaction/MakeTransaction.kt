package com.selegend.ecommercemobile.store.domain.model.core.transaction

data class MakeTransaction(
    val orderId: Int,
    val nonce: String
)
