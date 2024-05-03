package com.example.ecommercemobile.store.domain.model

data class Response<T>(
    val statusCode: Int,
    val message: String,
    val stack: String ?= null,
    val metadata: T
)