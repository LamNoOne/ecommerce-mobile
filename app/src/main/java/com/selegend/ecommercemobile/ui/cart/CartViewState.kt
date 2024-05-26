package com.selegend.ecommercemobile.ui.cart

import com.selegend.ecommercemobile.store.domain.model.core.carts.Cart

data class CartViewState(
    val isLoading: Boolean = false,
    val cart: Cart? = null,
    val error: String? = null
)