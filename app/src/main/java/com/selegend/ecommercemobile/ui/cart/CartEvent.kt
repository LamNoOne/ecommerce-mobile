package com.selegend.ecommercemobile.ui.cart

import com.selegend.ecommercemobile.store.domain.model.core.carts.ProductCart

sealed class CartEvent {

    data class OnCartProductClick(val productId: Int): CartEvent()

    data class OnCartProductDelete(val productId: Int): CartEvent()

    data class OnChangeProductQuantity(val productId: Int, val quantity: Int): CartEvent()
    data class OnProductClick(val productId: Int): CartEvent()

    data class OnCheckoutClick(val productCheckout: List<ProductCart>): CartEvent()

    data class OnShowSnackBar(val message: String): CartEvent()

}