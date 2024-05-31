package com.selegend.ecommercemobile.ui.payment

sealed class PaymentEvent {
    object OnClickContinue: PaymentEvent()
}