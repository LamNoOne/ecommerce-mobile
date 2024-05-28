package com.selegend.ecommercemobile.ui.home.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selegend.ecommercemobile.store.domain.repository.ProductsRepository
import com.selegend.ecommercemobile.ui.home.events.SearchProductEvent
import com.selegend.ecommercemobile.ui.home.viewstates.SearchProductViewState
import com.selegend.ecommercemobile.ui.utils.UIEvent
import com.selegend.ecommercemobile.utils.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
): ViewModel() {
    // set mutable state
    private var _state = MutableStateFlow(SearchProductViewState())

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    // get immutable one
    val state = _state.asStateFlow()

    init {
        getProducts()
    }

    private fun getProducts(query: String ?= null) {
        val search = query ?: "mac"
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            productsRepository.searchProduct(search, 1, 10, "", "")
                .onRight { products ->
                    Log.d("SearchViewModel", "getProducts: ${products.metadata.products}")
                    _state.update { currentState ->
                        currentState.copy(products = products.metadata.products)
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

    fun onEvent(event: SearchProductEvent) {
        when(event) {
            is SearchProductEvent.OnProductClick -> {
                sendUIEvent(UIEvent.Navigate("${Routes.PRODUCT_DETAIL}?productId=${event.productId}"))
            }
            is SearchProductEvent.OnSubmitSearch -> {
                sendUIEvent(UIEvent.Navigate("${Routes.PRODUCT}?search=${event.search}"))
            }
            is SearchProductEvent.OnSearchQueryChange -> {
                getProducts(event.query)
            }
        }
    }

    private fun sendUIEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}