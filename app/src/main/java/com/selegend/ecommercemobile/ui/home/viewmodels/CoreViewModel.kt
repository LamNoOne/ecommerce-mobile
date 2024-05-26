package com.selegend.ecommercemobile.ui.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selegend.ecommercemobile.ui.home.events.CoreEvent
import com.selegend.ecommercemobile.ui.utils.UIEvent
import com.selegend.ecommercemobile.utils.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoreViewModel @Inject constructor(): ViewModel() {

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: CoreEvent) {
        when(event) {
            is CoreEvent.OnHomeClick -> {
                sendUIEvent(UIEvent.Navigate(Routes.HOME))
            }
            is CoreEvent.OnFavoriteClick -> {
                sendUIEvent(UIEvent.Navigate(Routes.FAVORITE))
            }
            is CoreEvent.OnCartClick -> {
                sendUIEvent(UIEvent.Navigate(Routes.CART))
            }
            is CoreEvent.OnProfileClick -> {
                sendUIEvent(UIEvent.Navigate(Routes.PROFILE))
            }
            is CoreEvent.OnLoginClick -> {
                sendUIEvent(UIEvent.Navigate(Routes.LOGIN))
            }
            is CoreEvent.OnSearchClick -> {
                sendUIEvent(UIEvent.Navigate(Routes.SEARCH))
            }
        }
    }

    private fun sendUIEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}