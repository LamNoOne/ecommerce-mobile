package com.selegend.ecommercemobile.ui.product_detail

import com.selegend.ecommercemobile.store.domain.model.core.carts.Cart

data class ProductDetailCartViewState(
    val isLoading: Boolean = false,
    val cart: Cart? = null,
    val error: String? = null
)