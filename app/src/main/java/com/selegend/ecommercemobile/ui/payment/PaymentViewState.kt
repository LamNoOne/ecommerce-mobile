package com.selegend.ecommercemobile.ui.payment

import com.selegend.ecommercemobile.store.domain.model.core.orders.OrderCheckout

data class PaymentViewState (
    val isLoading: Boolean = false,
    val order: OrderCheckout? = null,
    val error: String? = null
)