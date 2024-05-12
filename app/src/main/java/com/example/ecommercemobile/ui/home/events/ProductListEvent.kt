package com.example.ecommercemobile.ui.home.events

sealed class ProductListEvent {
    data class OnProductClick(val productId: Int): ProductListEvent()
    data class OnWishListProductClick(val productId: Int): ProductListEvent()
}