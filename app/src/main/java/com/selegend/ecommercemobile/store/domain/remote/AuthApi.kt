package com.selegend.ecommercemobile.store.domain.remote

import com.selegend.ecommercemobile.store.domain.model.MetadataAuth
import com.selegend.ecommercemobile.store.domain.model.Response
import com.selegend.ecommercemobile.store.domain.model.core.auth.LoginCredentials
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/sign-in")
    suspend fun signIn(@Body loginCredentials: LoginCredentials): Response<MetadataAuth>
}