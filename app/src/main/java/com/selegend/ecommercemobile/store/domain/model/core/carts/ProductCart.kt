package com.selegend.ecommercemobile.store.domain.model.core.carts

import com.selegend.ecommercemobile.store.domain.model.core.products.Product

data class ProductCart(
    val quantity: Int,
    val product: Product,
    val checked: Boolean = false
)