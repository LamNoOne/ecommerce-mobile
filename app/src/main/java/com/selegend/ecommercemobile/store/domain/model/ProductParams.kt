package com.selegend.ecommercemobile.store.domain.model

data class ProductParams(
    val name: String,
    val categoryId: Int,
    val limit: Int,
    val sortBy: String,
    val order: String
)