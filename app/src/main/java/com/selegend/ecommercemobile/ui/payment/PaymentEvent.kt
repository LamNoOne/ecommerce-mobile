package com.selegend.ecommercemobile.ui.payment

sealed class PaymentEvent {
    object OnClickContinue: PaymentEvent()

    data class OnClickViewOrder(val transactionId: String): PaymentEvent()
}