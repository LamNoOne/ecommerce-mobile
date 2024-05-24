package com.example.ecommercemobile.store.domain.model

import com.example.ecommercemobile.store.domain.model.core.orders.OrderCheckout

data class MetadataOrders(
    val page: Int ?= null,
    val total: Int ?= null,
    val totalPage: Int ?= null,
    val orders: List<OrderCheckout>
)