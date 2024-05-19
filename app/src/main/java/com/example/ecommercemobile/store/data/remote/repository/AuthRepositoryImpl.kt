package com.example.ecommercemobile.store.data.remote.repository

import arrow.core.Either
import com.example.ecommercemobile.core.NetworkError
import com.example.ecommercemobile.store.data.mapper.toNetworkError
import com.example.ecommercemobile.store.data.remote.AuthApi
import com.example.ecommercemobile.store.data.remote.AuthDao
import com.example.ecommercemobile.store.domain.model.MetadataAuth
import com.example.ecommercemobile.store.domain.model.Response
import com.example.ecommercemobile.store.domain.model.core.auth.Auth
import com.example.ecommercemobile.store.domain.model.core.auth.LoginCredentials
import com.example.ecommercemobile.store.domain.repository.AuthRepository
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