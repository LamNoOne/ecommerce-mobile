package com.selegend.ecommercemobile.store.domain.model.core.auth

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.selegend.ecommercemobile.utils.Constants

@Entity
data class Auth(
    val userId: Int,
    val lastName: String,
    val firstName: String,
    val username: String?,
    val image: String?,
    val phoneNumber: String?,
    val email: String,
    val address: String,
    val accessToken: String,
    val refreshToken: String,
    @PrimaryKey val id: Int = Constants.TOKEN_UID
)