package com.selegend.ecommercemobile.store.domain.model.core.transaction

data class Customer(
    val company: Any,
    val email: String,
    val fax: Any,
    val firstName: String,
    val id: Any,
    val internationalPhone: InternationalPhone,
    val lastName: String,
    val phone: String,
    val website: Any
)