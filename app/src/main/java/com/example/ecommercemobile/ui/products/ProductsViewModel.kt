package com.example.ecommercemobile.ui.products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.ecommercemobile.store.domain.model.ProductParams
import com.example.ecommercemobile.store.domain.repository.ProductsRepository
import com.example.ecommercemobile.ui.events.ProductListEvent
import com.example.ecommercemobile.ui.utils.UIEvent
import com.example.ecommercemobile.utils.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val categoryId = savedStateHandle.get<Int>("categoryId") ?: ""
    private val search = savedStateHandle.get<String>("search") ?: ""
    private val sortBy = savedStateHandle.get<String>("sortBy") ?: ""
    private val order = savedStateHandle.get<String>("order") ?: ""

    private val productParams = ProductParams(
        name = search,
        categoryId = categoryId as Int,
        limit = 20,
        sortBy = sortBy,
        order = order
    )

    val productPager = Pager(PagingConfig(pageSize = 20)) {
        ProductsDataSource(productsRepository, productParams)
    }.flow.cachedIn(viewModelScope)

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