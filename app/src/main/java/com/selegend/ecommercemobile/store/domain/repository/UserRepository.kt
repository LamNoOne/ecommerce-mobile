package com.selegend.ecommercemobile.store.domain.repository

import arrow.core.Either
import com.selegend.ecommercemobile.core.NetworkError
import com.selegend.ecommercemobile.store.domain.model.MetadataUser
import com.selegend.ecommercemobile.store.domain.model.Response
import com.selegend.ecommercemobile.store.domain.model.core.users.User
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface UserRepository {
    suspend fun getUserInfo(
        headers: Map<String, String>
    ): Either<NetworkError, Response<MetadataUser>>

    suspend fun updateUserInfo(
        headers: Map<String, String>,
        image: MultipartBody.Part,
        partMap: MutableMap<String, RequestBody>
    ): Either<NetworkError, Response<User>>

    suspend fun updateUserImage(
        headers: Map<String, String>,
        image: MultipartBody.Part
    ): Either<NetworkError, Response<User>>

    suspend fun updateUserCredentials(
        headers: Map<String, String>,
        partMap: MutableMap<String, RequestBody>
    ): Either<NetworkError, Response<User>>
}