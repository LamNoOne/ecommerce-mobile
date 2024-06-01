package com.selegend.ecommercemobile.ui.cart

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.selegend.ecommercemobile.store.domain.model.CartProductParams
import com.selegend.ecommercemobile.store.domain.model.core.auth.Auth
import com.selegend.ecommercemobile.store.domain.model.core.carts.UpdateCart
import com.selegend.ecommercemobile.store.domain.repository.AuthRepository
import com.selegend.ecommercemobile.store.domain.repository.CartRepository
import com.selegend.ecommercemobile.store.domain.repository.ProductsRepository
import com.selegend.ecommercemobile.ui.utils.UIEvent
import com.selegend.ecommercemobile.ui.utils.sendEvent
import com.selegend.ecommercemobile.utils.Event
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
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val productsRepository: ProductsRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    // set mutable state of cart
    private var _state = MutableStateFlow(CartViewState())

    private var auth by mutableStateOf<Auth?>(null)
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

    private val productParams = CartProductParams(
        name = "",
        limit = 20,
        sortBy = "",
        order = ""
    )

    val productPager = Pager(
        config = PagingConfig(pageSize = 50)
    ) {
        CartProductDataSource(productsRepository, productParams)
    }.flow.cachedIn(viewModelScope)

    private fun getCart() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            cartRepository.getCart(getHeaderMap())
                .onRight { cart ->
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

    private fun deleteProductFromCart(productId: Int) {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            cartRepository.deleteProductFromCart(getHeaderMap(), productId)
                .onRight { cart ->
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

    private fun updateProductCartQuantity(updateCart: UpdateCart) {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            cartRepository.updateQuantityProduct(getHeaderMap(), updateCart)
                .onRight { cart ->
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
        when (event) {
            is CartEvent.OnCartProductClick -> {
                sendUIEvent(UIEvent.Navigate("${Routes.PRODUCT_DETAIL}?productId=${event.productId}"))
            }
            is CartEvent.OnCartProductDelete -> {
                deleteProductFromCart(event.productId)
            }
            is CartEvent.OnChangeProductQuantity -> {
                updateProductCartQuantity(UpdateCart(event.productId, event.quantity))
            }
            is CartEvent.OnProductClick -> {
                sendUIEvent(UIEvent.Navigate("${Routes.PRODUCT_DETAIL}?productId=${event.productId}"))
            }
            is CartEvent.OnCheckoutClick -> {
                var productIds = ""
                event.productCheckout.forEach {
                    productIds += "${it.product.id},"
                }
                sendUIEvent(UIEvent.Navigate("${Routes.CHECKOUT}?productIds=${productIds.dropLast(1)}"))
            }
            is CartEvent.OnShowSnackBar -> {
                sendEvent(Event.Toast(event.message))
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