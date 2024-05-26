package com.selegend.ecommercemobile.ui.home.viewstates

import com.selegend.ecommercemobile.store.domain.model.core.categories.Category

data class CategoryViewState(
    val isLoading: Boolean = false,
    val categories: List<Category> = listOf(),
    val error: String? = null
)
