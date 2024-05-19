package com.example.ecommercemobile.ui.product_detail

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercemobile.store.domain.model.core.auth.Auth
import com.example.ecommercemobile.store.domain.model.core.carts.AddCart
import com.example.ecommercemobile.store.domain.repository.AuthRepository
import com.example.ecommercemobile.store.domain.repository.CartRepository
import com.example.ecommercemobile.store.domain.repository.ProductsRepository
import com.example.ecommercemobile.ui.utils.UIEvent
import com.example.ecommercemobile.utils.Event
import com.example.ecommercemobile.utils.EventBus
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
class ProductDetailViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val cartRepository: CartRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    // set mutable state
    private var _state = MutableStateFlow(ProductDetailViewState())

    // get immutable one
    val state = _state.asStateFlow()

    private var auth by mutableStateOf<Auth?>(null)

    /**
     * Channel for UI events.
     */
    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val productId = savedStateHandle.get<Int>("productId")
        productId?.let {
            getProductById(it)
        }

        runBlocking {
            authRepository.getAuth(1)?.let {
                auth = it
            }
        }

        Log.d("ProductDetailViewModel", "init: $auth")
    }

    private fun getProductById(id: Int) {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            productsRepository.getProductById(id)
                .onRight { productResponse ->
                    _state.update {
                        it.copy(product = productResponse.metadata.product)
                    }
                }
                .onLeft { err ->
                    _state.update {
                        it.copy(error = err.error.message)
                    }
                    EventBus.sendEvent(Event.Toast(err.error.message))
                }
            _state.update {
                it.copy(isLoading = false)
            }
        }
    }

    fun onEvent(event: ProductDetailEvent) {
        when (event) {
            is ProductDetailEvent.OnAddToCartClick -> {
                viewModelScope.launch {
                    cartRepository.addToCart(
                        getHeaderMap(),
                        AddCart(event.productId, event.quantity)
                    )
                        .onRight {
                            EventBus.sendEvent(Event.Toast("Add to cart successfully!"))
                        }
                        .onLeft { err ->
                            EventBus.sendEvent(Event.Toast(err.error.message))
                        }
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