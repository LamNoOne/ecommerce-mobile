package com.example.ecommercemobile.store.domain.model.core

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.ecommercemobile.utils.Constants

@Entity
data class Auth(
    val userId: Int,
    val username: String,
    val email: String,
    val image: String,
    val accessToken: String,
    val refreshToken: String,
    @PrimaryKey val id: Int = Constants.TOKEN_UID
)