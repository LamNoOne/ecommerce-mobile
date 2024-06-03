package com.selegend.ecommercemobile.ui.auth

import com.selegend.ecommercemobile.store.domain.model.core.auth.LoginCredentials

sealed class AuthEvent {
    // HAndle event
    data class OnUsernameChange(val username: String) : AuthEvent()
    data class OnPasswordChange(val password: String) : AuthEvent()
    data class OnLoginClick(val loginCredentials: LoginCredentials) : AuthEvent()
    object OnSignUpClick : AuthEvent()
    object OnLoginNavigateClick : AuthEvent()
    object OnSignUpNavigateClick : AuthEvent()
    data class OnFirstNameChange(val firstName: String) : AuthEvent()
    data class OnLastNameChange(val lastName: String) : AuthEvent()
    data class OnEmailChange(val email: String) : AuthEvent()
    data class OnPhoneNumberChange(val phoneNumber: String) : AuthEvent()
    data class OnUserNameSignupChange(val username: String) : AuthEvent()
    data class OnPasswordSignupChange(val password: String) : AuthEvent()
    data class OnConfirmPasswordChange(val confirmPassword: String) : AuthEvent()
    data class OnOauthClick(val oauthTokenId: String) : AuthEvent()
}