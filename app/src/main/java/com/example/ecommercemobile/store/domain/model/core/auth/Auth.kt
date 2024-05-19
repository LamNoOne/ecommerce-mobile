package com.example.ecommercemobile.store.domain.model.core.auth

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ecommercemobile.utils.Constants

@Entity
data class Auth(
    val userId: Int,
    val lastName: String,
    val firstName: String,
    val username: String?,
    val image: String?,
    val phoneNumber: String?,
    val email: String,
    val accessToken: String,
    val refreshToken: String,
    @PrimaryKey val id: Int = Constants.TOKEN_UID
)