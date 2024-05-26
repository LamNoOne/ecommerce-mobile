package com.selegend.ecommercemobile.store.data.remote.repository

import arrow.core.Either
import com.selegend.ecommercemobile.core.NetworkError
import com.selegend.ecommercemobile.store.data.mapper.toNetworkError
import com.selegend.ecommercemobile.store.data.remote.AuthApi
import com.selegend.ecommercemobile.store.data.remote.AuthDao
import com.selegend.ecommercemobile.store.domain.model.MetadataAuth
import com.selegend.ecommercemobile.store.domain.model.Response
import com.selegend.ecommercemobile.store.domain.model.core.auth.Auth
import com.selegend.ecommercemobile.store.domain.model.core.auth.LoginCredentials
import com.selegend.ecommercemobile.store.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDao: AuthDao,
    private val authApi: AuthApi
) : AuthRepository {
    override suspend fun insertAuth(auth: Auth) {
        authDao.insertAuth(auth)
    }

    override suspend fun deleteAuth(auth: Auth) {
        authDao.deleteAuth(auth)
    }

    override suspend fun getAuth(id: Int): Auth? {
        return authDao.getAuthById(id)
    }

    override suspend fun signIn(
        loginCredentials: LoginCredentials
    ): Either<NetworkError, Response<MetadataAuth>> {
        return Either.catch {
            authApi.signIn(loginCredentials)
        }.mapLeft { it.toNetworkError() }
    }
}