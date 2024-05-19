package com.example.ecommercemobile.store.domain.model.core.carts

data class Cart(
    val id: Int,
    val total: Int,
    val products: List<ProductCart>
)