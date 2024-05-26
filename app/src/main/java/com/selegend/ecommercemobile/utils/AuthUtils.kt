package com.selegend.ecommercemobile.utils

object AuthUtils {
    fun getHeaderMap(accessToken: String, userId: Int): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["Authorization"] = "Bearer $accessToken"
        headerMap["x-user-id"] = userId.toString()
        return headerMap
    }
}