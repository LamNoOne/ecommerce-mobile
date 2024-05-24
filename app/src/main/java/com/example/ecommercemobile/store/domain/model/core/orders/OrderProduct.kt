package com.example.ecommercemobile.store.domain.model.core.orders

import com.example.ecommercemobile.store.domain.model.core.products.Product

data class OrderProduct(
    val quantity: Int,
    val product: Product,
    val totalAmount: Int
)
