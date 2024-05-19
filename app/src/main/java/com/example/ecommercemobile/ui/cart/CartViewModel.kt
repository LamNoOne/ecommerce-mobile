package com.example.ecommercemobile.ui.cart

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercemobile.store.domain.model.core.auth.Auth
import com.example.ecommercemobile.store.domain.repository.AuthRepository
import com.example.ecommercemobile.store.domain.repository.CartRepository
import com.example.ecommercemobile.ui.utils.UIEvent
import com.example.ecommercemobile.utils.Routes
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
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    // set mutable state of cart
    private var _state = MutableStateFlow(CartViewState())

    private var auth by mutableStateOf<Auth?>(null)
        private set

    private var _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val state = _state.asStateFlow()

    init {
        runBlocking {
            authRepository.getAuth(1)?.let {
                auth = it
            }
        }

        Log.d("CartViewModel", "init: $auth")
        getCart()
    }

    private fun getCart() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            cartRepository.getCart(getHeaderMap())
                .onRight { cart ->
                    Log.d("CartViewModel", "getCart: $cart")
                    _state.update { currentState ->
                        currentState.copy(cart = cart.metadata.cart)
                    }
                }
                .onLeft { err ->
                    _state.update {
                        it.copy(error = err.error.message)
                    }
                }
            _state.update {
                it.copy(isLoading = false)
            }
        }
    }

    fun onEvent(event: CartEvent) {
        when(event) {
            is CartEvent.OnCartProductClick -> {
                sendUIEvent(UIEvent.Navigate("${Routes.PRODUCT_DETAIL}?productId=${event.productId}"))
            }
            is CartEvent.OnCartProductDelete -> {
                /* TODO ("Not yet implement") */
            }
            is CartEvent.OnChangeProductQuantity -> {
                /* TODO ("Not yet implement") */
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