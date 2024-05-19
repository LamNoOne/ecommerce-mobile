package com.example.ecommercemobile.ui.home.viewstates

import com.example.ecommercemobile.store.domain.model.core.products.Product

data class ProductCategoryViewState(
    val isLoading: Boolean = false,
    val products: Map<String, List<Product>> = mapOf(),
    val error: String? = null
)