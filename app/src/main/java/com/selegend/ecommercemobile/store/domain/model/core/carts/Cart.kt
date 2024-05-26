package com.selegend.ecommercemobile.store.domain.model.core.carts

import com.selegend.ecommercemobile.store.domain.model.core.payment.ProductPayment

data class Cart(
    val id: Int,
    val total: Int,
    val products: List<ProductPayment>
)