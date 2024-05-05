package com.example.ecommercemobile.ui.home

sealed class ProductListEvent {
    data class OnProductClick(val productId: Int): ProductListEvent()
    data class OnWishListProductClick(val productId: Int): ProductListEvent()
}