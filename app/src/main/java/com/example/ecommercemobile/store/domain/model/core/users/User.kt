package com.example.ecommercemobile.store.domain.model.core.users

data class User(
    val id: Int,
    val lastName: String,
    val firstName: String,
    val username: String,
    val image: String,
    val phoneNumber: String,
    val email: String,
    val address: String,
)
