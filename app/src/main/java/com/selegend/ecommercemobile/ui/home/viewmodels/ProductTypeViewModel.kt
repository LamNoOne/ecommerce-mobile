package com.selegend.ecommercemobile.ui.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selegend.ecommercemobile.store.domain.repository.ProductsRepository
import com.selegend.ecommercemobile.ui.events.ProductListEvent
import com.selegend.ecommercemobile.ui.home.viewstates.ProductCategoryViewState
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
class ProductTypeViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
) : ViewModel() {
    // set mutable state
    private var _smartphoneState = MutableStateFlow(ProductCategoryViewState())
    private var _laptopState = MutableStateFlow(ProductCategoryViewState())
    private var _accessoryState = MutableStateFlow(ProductCategoryViewState())
    private var _cameraState = MutableStateFlow(ProductCategoryViewState())
    private var _pcState = MutableStateFlow(ProductCategoryViewState())
    private var _studioState = MutableStateFlow(ProductCategoryViewState())
    private var _tvState = MutableStateFlow(ProductCategoryViewState())

    val smartphoneState = _smartphoneState.asStateFlow()
    val laptopState = _laptopState.asStateFlow()
    val accessoryState = _accessoryState.asStateFlow()
    val cameraState = _cameraState.asStateFlow()
    val pcState = _pcState.asStateFlow()
    val studioState = _studioState.asStateFlow()
    val tvState = _tvState.asStateFlow()

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        getProductCategory(_smartphoneState, 1)
        getProductCategory(_laptopState, 2)
        getProductCategory(_accessoryState, 3)
        getProductCategory(_studioState, 4)
        getProductCategory(_cameraState, 5)
        getProductCategory(_pcState, 6)
        getProductCategory(_tvState, 7)
    }


    private fun getProductCategory(_state: MutableStateFlow<ProductCategoryViewState>, id: Int) {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            productsRepository.getProductsByCategory(id, 1, 20)
                .onRight { products ->
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

    fun onEvent(event: ProductListEvent) {
        when (event) {
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