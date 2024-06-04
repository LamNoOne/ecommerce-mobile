package com.selegend.ecommercemobile.store.domain.remote

import com.selegend.ecommercemobile.store.domain.model.MetadataUser
import com.selegend.ecommercemobile.store.domain.model.Response
import com.selegend.ecommercemobile.store.domain.model.core.users.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface UserApi {
    @GET("users/get-info")
    suspend fun getUserInfo(@HeaderMap headers: Map<String, String>): Response<MetadataUser>

    @Multipart
    @PATCH("users/update-profile")
    suspend fun updateUserInfo(
        @HeaderMap headers: Map<String, String>,
        @Part image: MultipartBody.Part,
        @PartMap partMap: MutableMap<String, RequestBody>
    ): Response<User>

    @Multipart
    @PATCH("users/update-profile")
    suspend fun updateUserImage(
        @HeaderMap headers: Map<String, String>,
        @Part image: MultipartBody.Part
    ): Response<User>

    @Multipart
    @PATCH("users/update-profile")
    suspend fun updateUserCredentials(
        @HeaderMap headers: Map<String, String>,
        @PartMap partMap: MutableMap<String, RequestBody>
    ): Response<User>
}