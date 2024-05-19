package com.example.ecommercemobile.ui.cart

import com.example.ecommercemobile.store.domain.model.core.carts.Cart

data class CartViewState(
    val isLoading: Boolean = false,
    val cart: Cart? = null,
    val error: String? = null
)