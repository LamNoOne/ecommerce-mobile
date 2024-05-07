package com.example.ecommercemobile.ui.product_detail

import com.example.ecommercemobile.store.domain.model.core.Product

data class ProductDetailViewState(
    val isLoading: Boolean = false,
    val product: Product? = null,
    val error: String? = null
)