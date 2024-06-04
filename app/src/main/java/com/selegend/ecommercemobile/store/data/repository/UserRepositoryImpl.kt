package com.selegend.ecommercemobile.store.data.repository

import arrow.core.Either
import com.selegend.ecommercemobile.core.NetworkError
import com.selegend.ecommercemobile.store.data.mapper.toNetworkError
import com.selegend.ecommercemobile.store.domain.model.MetadataUser
import com.selegend.ecommercemobile.store.domain.model.Response
import com.selegend.ecommercemobile.store.domain.model.core.users.User
import com.selegend.ecommercemobile.store.domain.remote.UserApi
import com.selegend.ecommercemobile.store.domain.repository.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi
) : UserRepository {
    override suspend fun getUserInfo(headers: Map<String, String>): Either<NetworkError, Response<MetadataUser>> {
        return Either.catch {
            userApi.getUserInfo(headers)
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun updateUserInfo(
        headers: Map<String, String>,
        image: MultipartBody.Part,
        partMap: MutableMap<String, RequestBody>
    ): Either<NetworkError, Response<User>> {
        return Either.catch {
            userApi.updateUserInfo(headers, image, partMap)
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun updateUserImage(
        headers: Map<String, String>,
        image: MultipartBody.Part
    ): Either<NetworkError, Response<User>> {
        return Either.catch {
            userApi.updateUserImage(headers, image)
        }.mapLeft {
            it.toNetworkError()
        }
    }

    override suspend fun updateUserCredentials(
        headers: Map<String, String>,
        partMap: MutableMap<String, RequestBody>
    ): Either<NetworkError, Response<User>> {
        return Either.catch {
            userApi.updateUserCredentials(headers, partMap)
        }.mapLeft {
            it.toNetworkError()
        }
    }
}