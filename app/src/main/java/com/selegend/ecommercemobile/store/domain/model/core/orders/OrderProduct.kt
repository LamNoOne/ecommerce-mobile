package com.selegend.ecommercemobile.store.domain.model.core.orders

import com.selegend.ecommercemobile.store.domain.model.core.products.Product

data class OrderProduct(
    val quantity: Int,
    val product: Product,
    val totalAmount: Int
)
