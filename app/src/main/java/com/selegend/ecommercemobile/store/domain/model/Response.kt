package com.selegend.ecommercemobile.store.domain.model

// Generic response model => class, type
data class Response<T>(
    val statusCode: Int,
    val message: String ?= null,
    val stack: String ?= null,
    val metadata: T
)