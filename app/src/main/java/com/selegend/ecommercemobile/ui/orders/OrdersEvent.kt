package com.selegend.ecommercemobile.ui.orders

sealed class OrdersEvent {
    data class OnOrderClick(val orderId: Int): OrdersEvent()
    object OnSearchClick: OrdersEvent()
}