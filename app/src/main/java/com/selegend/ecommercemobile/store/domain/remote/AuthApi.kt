package com.selegend.ecommercemobile.store.domain.remote

import com.selegend.ecommercemobile.store.domain.model.Metadata
import com.selegend.ecommercemobile.store.domain.model.MetadataAuth
import com.selegend.ecommercemobile.store.domain.model.Response
import com.selegend.ecommercemobile.store.domain.model.core.auth.LoginCredentials
import com.selegend.ecommercemobile.store.domain.model.core.auth.OauthCredentials
import com.selegend.ecommercemobile.store.domain.model.core.auth.SignupCredentials
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    // Return success response with metadata
    @POST("auth/sign-in")
    suspend fun signIn(@Body loginCredentials: LoginCredentials): Response<MetadataAuth>

    @POST("auth/sign-up")
    suspend fun signUp(@Body signupCredentials: SignupCredentials): Response<Metadata>

    @POST("auth/sign-in/oauth")
    suspend fun oAuthenticate(@Body oauthCredentials: OauthCredentials): Response<MetadataAuth>
}