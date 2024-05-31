package com.selegend.ecommercemobile.store.domain.model.core.products

data class ProductType(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String,
    val products: List<Product>
)