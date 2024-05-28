package com.selegend.ecommercemobile.ui.products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.selegend.ecommercemobile.store.domain.model.ProductParams
import com.selegend.ecommercemobile.store.domain.repository.ProductsRepository
import com.selegend.ecommercemobile.ui.utils.UIEvent
import com.selegend.ecommercemobile.utils.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val categoryId = savedStateHandle.get<String>("categoryId") ?: ""
    val search = savedStateHandle.get<String>("search") ?: ""
    val sortBy = savedStateHandle.get<String>("sortBy") ?: ""
    val order = savedStateHandle.get<String>("order") ?: ""

    private val productParams = ProductParams(
        name = search,
        categoryId = categoryId,
        limit = 20,
        sortBy = sortBy,
        order = order
    )

    var productPager = Pager(PagingConfig(pageSize = 20)) {
        ProductsDataSource(productsRepository, productParams)
    }.flow.cachedIn(viewModelScope)

    fun onEvent(event: ProductsEvent) {
        when (event) {
            is ProductsEvent.OnProductClick -> {
                sendUIEvent(UIEvent.Navigate("${Routes.PRODUCT_DETAIL}?productId=${event.productId}"))
            }
            is ProductsEvent.OnWishListProductClick -> {
                TODO("Not yet implemented")
            }
            is ProductsEvent.OnBestMatchSelected -> {
                val productQuery = ProductParams(
                    name = productParams.name,
                    categoryId = productParams.categoryId,
                    limit = event.limit,
                    sortBy = event.sortBy,
                    order = event.order
                )
                productPager = Pager(PagingConfig(pageSize = 20)) {
                    ProductsDataSource(productsRepository, productQuery)
                }.flow.cachedIn(viewModelScope)
            }
            is ProductsEvent.OnPriceHighToLowSelected -> {
                val productQuery = ProductParams(
                    name = productParams.name,
                    categoryId = productParams.categoryId,
                    limit = event.limit,
                    sortBy = event.sortBy,
                    order = event.order
                )
                productPager = Pager(PagingConfig(pageSize = 20)) {
                    ProductsDataSource(productsRepository, productQuery)
                }.flow.cachedIn(viewModelScope)
            }
            is ProductsEvent.OnPriceLowToHighSelected -> {
                val productQuery = ProductParams(
                    name = productParams.name,
                    categoryId = productParams.categoryId,
                    limit = event.limit,
                    sortBy = event.sortBy,
                    order = event.order
                )
                productPager = Pager(PagingConfig(pageSize = 20)) {
                    ProductsDataSource(productsRepository, productQuery)
                }.flow.cachedIn(viewModelScope)
            }
        }
    }

    private fun sendUIEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}