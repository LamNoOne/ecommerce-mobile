package com.example.ecommercemobile.ui.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercemobile.store.domain.repository.ProductsRepository
import com.example.ecommercemobile.ui.events.ProductListEvent
import com.example.ecommercemobile.ui.home.viewstates.ProductCategoryViewState
import com.example.ecommercemobile.ui.utils.UIEvent
import com.example.ecommercemobile.utils.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
@HiltViewModel
class TVViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
) : ViewModel() {
    // set mutable state
    private var _state = MutableStateFlow(ProductCategoryViewState())

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    // get immutable one
    val state = _state.asStateFlow()


    fun init() {
        getProductCategory(7)
    }

    private fun getProductCategory(id: Int) {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            productsRepository.getProductsByCategory(id,1, 20)
                .onRight { products ->
                    _state.update { currentState ->
                        val updatedProducts = currentState.products.toMutableMap()
                        val currentProducts = products.metadata.products
                        updatedProducts[currentProducts.name] = currentProducts.products
                        currentState.copy(products = updatedProducts)
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

    fun onEvent(event: ProductListEvent) {
        when(event) {
            is ProductListEvent.OnProductClick -> {
                sendUIEvent(UIEvent.Navigate("${Routes.PRODUCT_DETAIL}?productId=${event.productId}"))
            }
            is ProductListEvent.OnWishListProductClick -> {
                TODO("Not yet implemented")
            }
        }
    }

    private fun sendUIEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}