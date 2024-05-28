package com.selegend.ecommercemobile.store.data.remote

import com.selegend.ecommercemobile.store.domain.model.MetadataUser
import com.selegend.ecommercemobile.store.domain.model.Response
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface UserApi {
    @GET("users/get-info")
    suspend fun getUserInfo(@HeaderMap headers: Map<String, String>): Response<MetadataUser>

    @Multipart
    @PATCH("users/update-info")
    suspend fun updateUserInfo(
        @HeaderMap headers: Map<String, String>,
        @Part image: MultipartBody.Part,
        @Part("firstName")  firstName: RequestBody,
        @Part("lastName") lastName: RequestBody,
        @Part("address") address: RequestBody,
    ): Response<MetadataUser>
}