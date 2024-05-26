package com.selegend.ecommercemobile.ui.checkout

import com.selegend.ecommercemobile.store.domain.model.core.carts.Cart

data class CheckoutViewState(
    val isLoading: Boolean = false,
    val cart: Cart? = null,
    val error: String? = null
)