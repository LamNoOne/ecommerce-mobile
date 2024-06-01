package com.selegend.ecommercemobile.store.domain.model.core.auth

data class SignupCredentials(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val username: String,
    val password: String,
)
