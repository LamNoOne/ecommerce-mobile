package com.selegend.ecommercemobile.store.domain.model

import com.selegend.ecommercemobile.store.domain.model.core.transaction.Transaction

data class MetadataMakeTransaction(
    val success: Boolean,
    val transaction: Transaction
)
