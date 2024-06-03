package com.selegend.ecommercemobile.store.domain.model.core.auth

data class OauthCredentials(
    val oauthId: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val imageUrl: String,
    val username: String
)