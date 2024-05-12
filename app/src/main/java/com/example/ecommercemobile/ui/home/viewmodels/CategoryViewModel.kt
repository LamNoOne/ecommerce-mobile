package com.example.ecommercemobile.ui.home.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercemobile.store.domain.repository.CategoriesRepository
import com.example.ecommercemobile.ui.home.events.CategoryEvent
import com.example.ecommercemobile.ui.home.viewstates.CategoryViewState
import com.example.ecommercemobile.ui.utils.UIEvent
import com.example.ecommercemobile.ui.utils.sendEvent
import com.example.ecommercemobile.utils.Event
import com.example.ecommercemobile.utils.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoriesRepository
) : ViewModel() {
    // set mutable state
    private var _state = MutableStateFlow(CategoryViewState())

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    // get immutable one
    val state = _state.asStateFlow()

    init {
        getCategories()
    }

    private fun getCategories() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            categoryRepository.getCategories()
                .onRight { categories ->
                    _state.update {
                        it.copy(categories = categories.metadata.categories)
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

    fun onEvent(event: CategoryEvent) {
        when (event) {
            is CategoryEvent.OnCategoryClick -> {
                sendUIEvent(UIEvent.Navigate("${Routes.PRODUCT}?categoryId=${event.categoryId}"))
            }
        }
    }

    private fun sendUIEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}