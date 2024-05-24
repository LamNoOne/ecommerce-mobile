package com.example.ecommercemobile.ui.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.example.ecommercemobile.store.domain.model.MetadataAuth
import com.example.ecommercemobile.store.domain.model.Response
import com.example.ecommercemobile.store.domain.model.core.auth.Auth
import com.example.ecommercemobile.store.domain.model.core.auth.LoginCredentials
import com.example.ecommercemobile.store.domain.repository.AuthRepository
import com.example.ecommercemobile.ui.utils.UIEvent
import com.example.ecommercemobile.utils.Constants
import com.example.ecommercemobile.utils.Event
import com.example.ecommercemobile.utils.EventBus
import com.example.ecommercemobile.utils.Routes
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

    var username by mutableStateOf<String>("")
        private set

    var password by mutableStateOf<String>("")
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
                /* TODO("Not yet implement") */
            }
            is AuthEvent.OnLoginNavigateClick -> {
                /* TODO("Not yet implement") */
            }
            is AuthEvent.OnSignUpNavigateClick -> {
                /* TODO("Not yet implement") */
            }
        }
    }

    private fun sendUIEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}