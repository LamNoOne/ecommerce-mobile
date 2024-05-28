package com.selegend.ecommercemobile.ui.product_detail

sealed class ProductDetailEvent {
    data class OnAddToCartClick(val productId: Int, val quantity: Int): ProductDetailEvent()

    object OnCartClick: ProductDetailEvent()
}