package com.example.ecommercemobile.ui.auth

import com.example.ecommercemobile.store.domain.model.core.auth.LoginCredentials
import com.example.ecommercemobile.store.domain.model.core.auth.SignupCredentials

sealed class AuthEvent {
    data class OnUsernameChange(val username: String) : AuthEvent()
    data class OnPasswordChange(val password: String) : AuthEvent()
    data class OnLoginClick(val loginCredentials: LoginCredentials) : AuthEvent()
    data class OnSignUpClick(val signupCredentials: SignupCredentials) : AuthEvent()
    object OnLoginNavigateClick : AuthEvent()
    object OnSignUpNavigateClick : AuthEvent()
}