package com.selegend.ecommercemobile.datastore

data class AuthPreferences(
    val userId: Int,
    val accessToken: String,
    val refreshToken: String,
)