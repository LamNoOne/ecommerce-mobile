package com.selegend.ecommercemobile.store.domain.model.core.payment

import com.selegend.ecommercemobile.store.domain.model.core.products.Product

data class ProductPayment(
    val quantity: Int,
    val product: Product,
    val checked: Boolean = false
)