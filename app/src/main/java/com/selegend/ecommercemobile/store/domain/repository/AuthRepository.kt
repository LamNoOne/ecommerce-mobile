package com.selegend.ecommercemobile.store.domain.repository

import arrow.core.Either
import com.selegend.ecommercemobile.core.NetworkError
import com.selegend.ecommercemobile.store.domain.model.MetadataAuth
import com.selegend.ecommercemobile.store.domain.model.Response
import com.selegend.ecommercemobile.store.domain.model.core.auth.Auth
import com.selegend.ecommercemobile.store.domain.model.core.auth.LoginCredentials

/**
 * Store user authentication data. Only one user can be authenticated at a time.
 */
interface AuthRepository {
    suspend fun insertAuth(auth: Auth)

    suspend fun deleteAuth(auth: Auth)

    suspend fun getAuth(id: Int): Auth?

    suspend fun signIn(loginCredentials: LoginCredentials): Either<NetworkError, Response<MetadataAuth>>
}