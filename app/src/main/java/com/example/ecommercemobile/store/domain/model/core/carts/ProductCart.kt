package com.example.ecommercemobile.store.domain.model.core.carts

import com.example.ecommercemobile.store.domain.model.core.products.Product

data class ProductCart(
    val quantity: Int,
    val product: Product,
    val checked: Boolean ?= false
)