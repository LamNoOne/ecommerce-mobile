package com.selegend.ecommercemobile.store.domain.model

import com.selegend.ecommercemobile.store.domain.model.core.users.User

data class MetadataAuth(
    val user: User? = null,
    val accessToken: String ? = null,
    val refreshToken: String ? = null
)
