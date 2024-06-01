package com.selegend.ecommercemobile.store.domain.model

import com.selegend.ecommercemobile.store.domain.model.core.orders.OrderHistory
import com.selegend.ecommercemobile.store.domain.model.core.transaction.TransactionX

data class MetadataTransaction(
    val transaction: TransactionX,
    val order: OrderHistory
)