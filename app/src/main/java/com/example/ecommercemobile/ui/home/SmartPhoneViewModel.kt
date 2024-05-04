package com.example.ecommercemobile.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercemobile.store.domain.repository.ProductsRepository
import com.example.ecommercemobile.utils.Event
import com.example.ecommercemobile.utils.EventBus.sendEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
@HiltViewModel
class SmartPhoneViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
) : ViewModel() {
    // set mutable state
    private var _state = MutableStateFlow(ProductCategoryViewState())

    // get immutable one
    val state = _state.asStateFlow()

    fun init() {
        getProductCategory(1)
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
                    sendEvent(Event.Toast(err.error.message))
                }
            _state.update {
                it.copy(isLoading = false)
            }
        }
    }
}