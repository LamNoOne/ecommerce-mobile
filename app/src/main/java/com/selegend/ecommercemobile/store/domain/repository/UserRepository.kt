package com.selegend.ecommercemobile.store.domain.repository

import arrow.core.Either
import com.selegend.ecommercemobile.core.NetworkError
import com.selegend.ecommercemobile.store.domain.model.MetadataUser
import com.selegend.ecommercemobile.store.domain.model.Response
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface UserRepository {
    suspend fun getUserInfo(
        headers: Map<String, String>
    ): Either<NetworkError, Response<MetadataUser>>

    suspend fun updateUserInfo(
        headers: Map<String, String>,
        image: MultipartBody.Part,
        firstName: RequestBody,
        lastName: RequestBody,
        address: RequestBody
    ): Either<NetworkError, Response<MetadataUser>>
}