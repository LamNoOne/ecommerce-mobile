package com.selegend.ecommercemobile.ui.home.viewstates

import com.selegend.ecommercemobile.store.domain.model.core.products.ProductType

data class ProductCategoryViewState(
    val isLoading: Boolean = false,
    val products: ProductType ?= null,
    val error: String? = null
)