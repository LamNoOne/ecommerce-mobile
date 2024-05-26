package com.selegend.ecommercemobile.store.data.mapper

import com.selegend.ecommercemobile.core.ApiError
import com.selegend.ecommercemobile.core.NetworkError

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