package com.selegend.ecommercemobile.ui.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selegend.ecommercemobile.store.domain.model.core.auth.Auth
import com.selegend.ecommercemobile.store.domain.repository.AuthRepository
import com.selegend.ecommercemobile.store.domain.repository.UserRepository
import com.selegend.ecommercemobile.ui.utils.UIEvent
import com.selegend.ecommercemobile.utils.Event
import com.selegend.ecommercemobile.utils.EventBus
import com.selegend.ecommercemobile.utils.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private var auth by mutableStateOf<Auth?>(null)
    private var _state = MutableStateFlow(UserViewState())
    val state = _state.asStateFlow()

    fun updateState() {
        _state.update { it.copy() }
    }

    override fun onCleared() {
        super.onCleared()
        println("UserViewModel cleared")
    }

    /**
     * Channel for UI events.
     */
    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        runBlocking {
            authRepository.getAuth(1)?.let {
                auth = it
            }
        }
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            userRepository.getUserInfo(headers = getHeaderMap())
                .onRight { response ->
                    _state.update { it.copy(user = response.metadata.user) }
                }
                .onLeft { error ->
                    _state.update { it.copy(error = error.error.message) }
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

        fun onEvent(event: UserEvent) {
            when (event) {
                is UserEvent.OnEditProfileClick -> {
                    sendUIEvent(UIEvent.Navigate(Routes.PROFILE))
                }
                is UserEvent.OnCartClick -> {
                    sendUIEvent(UIEvent.Navigate(Routes.CART))
                }
                is UserEvent.OnFavoriteClick -> {

                }
                is UserEvent.OnOrderHistoryClick -> {
                    sendUIEvent(UIEvent.Navigate(Routes.ORDER_HISTORY))
                }
                is UserEvent.OnSettingsClick -> {

                }
                is UserEvent.OnLogoutClick -> {
                    viewModelScope.launch {
                        auth?.let { authRepository.deleteAuth(it) }
                        EventBus.sendEvent(Event.Toast("Logged out successfully"))
                        sendUIEvent(UIEvent.Navigate(Routes.LOGIN))
                    }
                }
            }
        }

        private fun getHeaderMap(): Map<String, String> {
            val headerMap = mutableMapOf<String, String>()
            headerMap["Authorization"] = "Bearer ${auth?.accessToken}"
            headerMap["x-user-id"] = auth?.userId.toString()
            return headerMap
        }

        private fun sendUIEvent(event: UIEvent) {
            viewModelScope.launch {
                _uiEvent.send(event)
            }
        }
    }