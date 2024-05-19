package com.example.ecommercemobile.ui.product_detail

sealed class ProductDetailEvent {
    data class OnAddToCartClick(val productId: Int, val quantity: Int): ProductDetailEvent()
}