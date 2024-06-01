package com.selegend.ecommercemobile.ui.orders

sealed class OrdersEvent {
    data class OnOrderClick(val transactionId: String): OrdersEvent()
    data class OnPaymentClick(val orderId: Int): OrdersEvent()
    object OnSearchClick: OrdersEvent()
}