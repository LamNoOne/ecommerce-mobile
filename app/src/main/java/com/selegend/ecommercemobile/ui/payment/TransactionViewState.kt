package com.selegend.ecommercemobile.ui.payment

data class TransactionViewState(
    val transactionId: String? = null,
    val orderId: Int ?= null,
    val success: Boolean,
)
