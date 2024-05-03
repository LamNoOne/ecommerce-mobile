package com.example.ecommercemobile.store.data.mapper

import com.example.ecommercemobile.core.ApiError
import com.example.ecommercemobile.core.NetworkError

import retrofit2.HttpException
import java.io.IOException

fun Throwable.toNetworkError(): NetworkError {
    val error = when(this) {
        is IOException -> ApiError.NETWORK_ERROR
        is HttpException -> ApiError.UNKNOWN_RESPONSE
        else -> ApiError.UNKNOWN_ERROR
    }
    return NetworkError(
        error = error,
        t = this
    )
}