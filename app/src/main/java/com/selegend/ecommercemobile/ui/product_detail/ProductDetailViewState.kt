package com.selegend.ecommercemobile.ui.product_detail

import com.selegend.ecommercemobile.store.domain.model.core.products.Product

data class ProductDetailViewState(
    val isLoading: Boolean = false,
    val product: Product? = null,
    val error: String? = null
)