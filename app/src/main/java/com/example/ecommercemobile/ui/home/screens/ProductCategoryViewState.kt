package com.example.ecommercemobile.ui.home.screens

import com.example.ecommercemobile.store.domain.model.core.Product

data class ProductCategoryViewState(
    val isLoading: Boolean = false,
    val products: Map<String, List<Product>> = mapOf(),
    val error: String? = null
)