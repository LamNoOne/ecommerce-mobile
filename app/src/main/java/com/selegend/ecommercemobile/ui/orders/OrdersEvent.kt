package com.selegend.ecommercemobile.ui.orders

sealed class OrdersEvent {
    data class OnOrderClick(val transactionId: String): OrdersEvent()
    object OnSearchClick: OrdersEvent()
}