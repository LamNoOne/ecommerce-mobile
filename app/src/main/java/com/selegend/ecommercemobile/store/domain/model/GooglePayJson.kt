package com.selegend.ecommercemobile.store.domain.model

import com.google.gson.annotations.SerializedName

data class Details(
    val bin: String,
    val cardType: String,
    val isNetworkTokenized: Boolean,
    val lastTwo: String,
    val lastFour: String
)

data class BinData(
    val prepaid: String,
    val healthcare: String,
    val debit: String,
    val durbinRegulated: String,
    val commercial: String,
    val payroll: String,
    val issuingBank: String,
    val countryOfIssuance: String,
    val productId: String
)

data class AndroidPayCard(
    val type: String,
    val nonce: String,
    val description: String,
    val consumed: Boolean,
    val threeDSecureInfo: Any?,
    val details: Details,
    val binData: BinData
)

data class GooglePayJson(
    @SerializedName("androidPayCards")
    val androidPayCards: List<AndroidPayCard>
)
