package com.selegend.ecommercemobile.store.domain.model.core.transaction

data class Shipping(
    val company: Any,
    val countryCodeAlpha2: String,
    val countryCodeAlpha3: String,
    val countryCodeNumeric: String,
    val countryName: String,
    val extendedAddress: Any,
    val firstName: String,
    val id: Any,
    val internationalPhone: InternationalPhone,
    val lastName: String,
    val locality: Any,
    val phoneNumber: String,
    val postalCode: String,
    val region: String,
    val shippingMethod: Any,
    val streetAddress: String
)