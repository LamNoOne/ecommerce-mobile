package com.example.ecommercemobile.ui.cart

sealed class CartEvent {

    data class OnCartProductClick(val productId: Int): CartEvent()

    data class OnCartProductDelete(val productId: Int): CartEvent()

    data class OnChangeProductQuantity(val productId: Int, val quantity: Int): CartEvent()
}