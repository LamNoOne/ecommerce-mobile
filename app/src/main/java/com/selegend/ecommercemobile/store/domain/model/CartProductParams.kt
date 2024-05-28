package com.selegend.ecommercemobile.store.domain.model

data class CartProductParams(
    val name: String,
    val limit: Int,
    val sortBy: String,
    val order: String
)