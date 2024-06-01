package com.selegend.ecommercemobile.store.domain.model.core.transaction

data class TransactionX(
    val id: String,
    val status: String,
    val type: String,
    val amount: String,
    val amountRequested: String,
    val merchantAccountId: String,
    val orderId: String,
    val last4: String,
    val cardType: String,
    val customer: Customer,
    val billing: Billing,
    val shipping: Shipping,
    val createdAt: String,
    val updatedAt: String
)