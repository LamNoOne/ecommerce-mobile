package com.selegend.ecommercemobile.store.domain.model.core.products

data class ProductCategory(
    val id: Int?,
    val name: String,
    val description: String?,
    val products: List<Product>
)