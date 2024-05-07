package com.example.ecommercemobile.ui.product_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    // set mutable state
    private var _state = MutableStateFlow(ProductDetailViewState())

    // get immutable one
    val state = _state.asStateFlow()

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
}