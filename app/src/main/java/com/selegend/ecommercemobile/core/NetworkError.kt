package com.selegend.ecommercemobile.core

data class NetworkError(
    val error: ApiError,
    val t: Throwable ?= null
)

enum class ApiError(val message: String) {
    NETWORK_ERROR("Network error"),
    UNKNOWN_RESPONSE("Unknown response"),
    UNKNOWN_ERROR("Unknown error")
}