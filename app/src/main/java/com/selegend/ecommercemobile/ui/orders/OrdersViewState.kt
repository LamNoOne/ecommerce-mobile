package com.selegend.ecommercemobile.ui.orders

import com.selegend.ecommercemobile.store.domain.model.core.orders.OrderHistory

data class OrdersViewState(
    val isLoading: Boolean = false,
    val orders: List<OrderHistory> = emptyList(),
    val error: Throwable? = null
)