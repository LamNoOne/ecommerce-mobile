package com.selegend.ecommercemobile.ui.home.viewstates

import com.selegend.ecommercemobile.store.domain.model.core.products.Product

data class SearchProductViewState (
    val isLoading: Boolean = false,
    val products: List<Product> ?= null,
    val error: String? = null
)