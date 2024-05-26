package com.selegend.ecommercemobile.ui.home.viewstates

import com.selegend.ecommercemobile.store.domain.model.core.products.Product

data class ProductCategoryViewState(
    val isLoading: Boolean = false,
    val products: Map<String, List<Product>> = mapOf(),
    val error: String? = null
)