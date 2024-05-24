package com.example.ecommercemobile.ui.checkout

import com.example.ecommercemobile.store.domain.model.core.checkout.CheckoutFromCart

sealed class CheckoutEvent {
    data class OnCreateOrder(val checkout: CheckoutFromCart) : CheckoutEvent()
}