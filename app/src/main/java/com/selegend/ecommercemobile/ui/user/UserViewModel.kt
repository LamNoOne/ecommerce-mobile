package com.selegend.ecommercemobile.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selegend.ecommercemobile.store.domain.repository.UserRepository
import com.selegend.ecommercemobile.ui.utils.UIEvent
import com.selegend.ecommercemobile.utils.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    /**
     * Channel for UI events.
     */
    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()



    fun onEvent(event: UserEvent) {
        when(event) {
            is UserEvent.OnEditProfileClick -> {

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
        }
    }


    private fun sendUIEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}