package com.selegend.ecommercemobile.ui.checkout

import com.selegend.ecommercemobile.store.domain.model.core.checkout.CheckoutFromCart

sealed class CheckoutEvent {
    data class OnCreateOrder(val checkout: CheckoutFromCart) : CheckoutEvent()
}