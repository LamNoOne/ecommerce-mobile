package com.selegend.ecommercemobile.ui.order_detail

import com.selegend.ecommercemobile.store.domain.model.core.orders.OrderHistory
import com.selegend.ecommercemobile.store.domain.model.core.transaction.TransactionX

data class OrderDetailViewState(
    val isLoading: Boolean ?= false,
    val transaction: TransactionX ?= null,
    val orders: OrderHistory ?= null,
    val error: String ?= null
)