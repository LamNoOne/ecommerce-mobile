package com.example.ecommercemobile.store.domain.model

import com.example.ecommercemobile.store.domain.model.core.User

data class MetadataAuth(
    val user: User ? = null,
    val accessToken: String ? = null,
    val refreshToken: String ? = null
)
