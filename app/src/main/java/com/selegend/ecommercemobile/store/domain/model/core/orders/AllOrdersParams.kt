package com.selegend.ecommercemobile.store.domain.model.core.orders

data class AllOrdersParams(
    val headers: Map<String, String>,
    val page: Int,
    val limit: Int,
    val sortBy: String,
    val order: String,
    val status: String,
    val name: String
)