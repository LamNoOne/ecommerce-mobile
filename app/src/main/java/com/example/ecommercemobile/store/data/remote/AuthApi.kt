package com.example.ecommercemobile.store.data.remote

import com.example.ecommercemobile.store.domain.model.MetadataAuth
import com.example.ecommercemobile.store.domain.model.Response
import com.example.ecommercemobile.store.domain.model.core.LoginCredentials
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/sign-in")
    suspend fun signIn(@Body loginCredentials: LoginCredentials): Response<MetadataAuth>
}