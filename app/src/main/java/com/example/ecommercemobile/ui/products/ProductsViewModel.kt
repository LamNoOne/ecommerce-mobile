package com.example.ecommercemobile.ui.products

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.ecommercemobile.store.domain.model.ProductParams
import com.example.ecommercemobile.store.domain.repository.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

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

    init {
        Log.d("Product Pager", "Product Pager: ${productPager.toString()}")
    }
}