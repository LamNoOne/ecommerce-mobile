package com.selegend.ecommercemobile.ui.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.selegend.ecommercemobile.store.domain.model.Metadata
import com.selegend.ecommercemobile.store.domain.model.MetadataAuth
import com.selegend.ecommercemobile.store.domain.model.Response
import com.selegend.ecommercemobile.store.domain.model.core.auth.Auth
import com.selegend.ecommercemobile.store.domain.model.core.auth.LoginCredentials
import com.selegend.ecommercemobile.store.domain.model.core.auth.SignupCredentials
import com.selegend.ecommercemobile.store.domain.repository.AuthRepository
import com.selegend.ecommercemobile.ui.utils.UIEvent
import com.selegend.ecommercemobile.utils.Constants
import com.selegend.ecommercemobile.utils.Event
import com.selegend.ecommercemobile.utils.EventBus
import com.selegend.ecommercemobile.utils.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    var auth by mutableStateOf<Auth?>(null)
        private set

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    /**
     * Login state
     */

    var username by mutableStateOf<String>("")
        private set

    var password by mutableStateOf<String>("")
        private set

    /**
     * Sign up state
     */

    var lastNameSignup by mutableStateOf<String>("")
        private set

    var firstNameSignup by mutableStateOf<String>("")
        private set

    var emailSignup by mutableStateOf<String>("")
        private set

    var phoneNumber by mutableStateOf<String>("")
        private set

    var usernameSignup by mutableStateOf<String>("")
        private set

    var passwordSignup by mutableStateOf<String>("")
        private set

    var confirmPasswordSignup by mutableStateOf<String>("")
        private set

    init {
        viewModelScope.launch {
            repository.getAuth(1)?.let {
                this@AuthViewModel.auth = it
            }
        }
    }

    private suspend fun signIn(loginCredentials: LoginCredentials): Response<MetadataAuth> {
        return when (val response = repository.signIn(loginCredentials)) {
            is Either.Right -> (response as Either.Right<Response<MetadataAuth>>).value
            else -> Response<MetadataAuth>(500, null, null, MetadataAuth())
        }
    }

    private suspend fun signUp(signupCredentials: SignupCredentials): Response<Metadata> {
        return when (val response = repository.signUp(signupCredentials)) {
            is Either.Right -> (response as Either.Right<Response<Metadata>>).value
            else -> Response<Metadata>(500, null, null, Metadata())
        }
    }

    /**
     * Call signIn
     * Check statusCode = 401 || 500 -> EventBus.sendEvent(Event.Toast("Can not sign in right row. Please try again!!!"))
     * Else: Store user authentication data => insert user data to database
     * EventBus.sendEvent(Event.Toast("Sign in successfully!!!"))
     * EventBus.sendEvent(Event.Navigate(Routes.HOME))
     * Read user data from database in Home when user is authenticated
     */
    private fun authenticate(loginCredentials: LoginCredentials) {
        viewModelScope.launch {
            val response = signIn(loginCredentials)
            Log.d("AuthViewModel", "authenticate: $response")
            if (response.statusCode == 401 || response.statusCode == 500) {
                EventBus.sendEvent(Event.Toast("Can not sign in right row. Please try again!!!"))
                return@launch
            }
            val auth = response.metadata.user?.let {
                response.metadata.accessToken?.let { it1 ->
                    response.metadata.refreshToken?.let { it2 ->
                        val (
                            userId,
                            lastName,
                            firstName,
                            username,
                            image,
                            phoneNumber,
                            email,
                            address
                        ) = response.metadata.user
                        Auth(
                            userId = userId,
                            lastName = lastName,
                            firstName = firstName,
                            username = username,
                            image = image,
                            phoneNumber = phoneNumber,
                            email = email,
                            address = address,
                            accessToken = it1,
                            refreshToken = it2,
                            id = Constants.TOKEN_UID
                        )
                    }
                }
            }
            if (auth == null) {
                EventBus.sendEvent(Event.Toast("Can not sign in right row. Please try again!!!"))
                return@launch
            }
            repository.insertAuth(auth)
            EventBus.sendEvent(Event.Toast("Sign in successfully!!!"))
            sendUIEvent(UIEvent.Navigate(Routes.HOME))
        }
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.OnUsernameChange -> {
                username = event.username
            }
            is AuthEvent.OnPasswordChange -> {
                password = event.password
            }
            is AuthEvent.OnLoginClick -> {
                if (username.isEmpty() || password.isEmpty()) {
                    sendUIEvent(UIEvent.ShowSnackBar(message = "Username or password is empty!!!"))
                    return
                }
                authenticate(LoginCredentials(username, password))
            }
            is AuthEvent.OnSignUpClick -> {
                viewModelScope.launch {
                    val response = signUp(
                        SignupCredentials(
                            firstNameSignup,
                            lastNameSignup,
                            emailSignup,
                            phoneNumber,
                            usernameSignup,
                            passwordSignup
                        )
                    )
                    if (response.statusCode == 401 || response.statusCode == 500) {
                        EventBus.sendEvent(Event.Toast("Can not sign up right row. Please try again!!!"))
                        return@launch
                    }
                    if (response.statusCode == 201) {
                        EventBus.sendEvent(Event.Toast("Sign up successfully!!!"))
                        sendUIEvent(UIEvent.Navigate(Routes.LOGIN))
                    } else {
                        EventBus.sendEvent(Event.Toast("Can not sign up right row. Please try again!!!"))
                        return@launch
                    }
                }
            }
            is AuthEvent.OnLoginNavigateClick -> {
                /* TODO("Not yet implement") */
            }
            is AuthEvent.OnSignUpNavigateClick -> {
                /* TODO("Not yet implement") */
            }
            is AuthEvent.OnFirstNameChange -> {
                firstNameSignup = event.firstName
            }
            is AuthEvent.OnLastNameChange -> {
                lastNameSignup = event.lastName
            }
            is AuthEvent.OnEmailChange -> {
                emailSignup = event.email
            }
            is AuthEvent.OnPhoneNumberChange -> {
                phoneNumber = event.phoneNumber
            }
            is AuthEvent.OnUserNameSignupChange -> {
                usernameSignup = event.username
            }
            is AuthEvent.OnPasswordSignupChange -> {
                passwordSignup = event.password
            }
            is AuthEvent.OnConfirmPasswordChange -> {
                confirmPasswordSignup = event.confirmPassword
            }
        }
    }

    private fun sendUIEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}