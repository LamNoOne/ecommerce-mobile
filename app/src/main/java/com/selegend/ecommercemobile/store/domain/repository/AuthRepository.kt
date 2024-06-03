package com.selegend.ecommercemobile.store.domain.repository

import arrow.core.Either
import com.selegend.ecommercemobile.core.NetworkError
import com.selegend.ecommercemobile.store.domain.model.Metadata
import com.selegend.ecommercemobile.store.domain.model.MetadataAuth
import com.selegend.ecommercemobile.store.domain.model.Response
import com.selegend.ecommercemobile.store.domain.model.core.auth.Auth
import com.selegend.ecommercemobile.store.domain.model.core.auth.LoginCredentials
import com.selegend.ecommercemobile.store.domain.model.core.auth.OauthCredentials
import com.selegend.ecommercemobile.store.domain.model.core.auth.SignupCredentials

/**
 * Store user authentication data. Only one user can be authenticated at a time.
 */
interface AuthRepository {
    suspend fun insertAuth(auth: Auth)

    suspend fun deleteAuth(auth: Auth)

    suspend fun getAuth(id: Int): Auth?
    /// No yet =. Room DB

    suspend fun signIn(loginCredentials: LoginCredentials): Either<NetworkError, Response<MetadataAuth>>

    suspend fun signUp(signupCredentials: SignupCredentials): Either<NetworkError, Response<Metadata>>

    suspend fun oAuthenticate(oauthCredentials: OauthCredentials): Either<NetworkError, Response<MetadataAuth>>
}