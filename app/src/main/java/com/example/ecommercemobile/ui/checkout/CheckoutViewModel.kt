package com.example.ecommercemobile.ui.checkout

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercemobile.store.domain.model.core.auth.Auth
import com.example.ecommercemobile.store.domain.model.core.carts.GetSelectedProduct
import com.example.ecommercemobile.store.domain.repository.AuthRepository
import com.example.ecommercemobile.store.domain.repository.CartRepository
import com.example.ecommercemobile.store.domain.repository.CheckoutRepository
import com.example.ecommercemobile.ui.utils.UIEvent
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
class CheckoutViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val authRepository: AuthRepository,
    private val checkoutRepository: CheckoutRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    // set mutable state
    private var _state = MutableStateFlow(CheckoutViewState())

    // get immutable one
    val state = _state.asStateFlow()

    private var auth by mutableStateOf<Auth?>(null)

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

        val setOfProductId = savedStateHandle.get<String>("productIds")
        setOfProductId?.let { it ->
            val listOfProductIds = it.split(",").map { it.toInt() }
            val selectedProducts = GetSelectedProduct(listOfProductIds)
            getProduct(selectedProducts)
        }
    }

    private fun getProduct(selectedProducts: GetSelectedProduct) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            cartRepository.getSelectedProducts(getHeaderMap(), selectedProducts)
                .onRight { cart ->
                    Log.d("CheckoutViewModel", "getCart: $cart")
                    _state.update { currentState ->
                        currentState.copy(cart = cart.metadata.cart)
                    }
                }
                .onLeft { err ->
                    Log.d("CheckoutViewModel", "getCart error: $err")
                    _state.update {
                        it.copy(error = err.error.message)
                    }
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    private fun getHeaderMap(): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["Authorization"] = "Bearer ${auth?.accessToken}"
        headerMap["x-user-id"] = auth?.userId.toString()
        return headerMap
    }
}