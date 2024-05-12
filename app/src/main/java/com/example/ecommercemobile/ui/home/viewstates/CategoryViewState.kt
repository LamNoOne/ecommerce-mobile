package com.example.ecommercemobile.ui.home.viewstates

import com.example.ecommercemobile.store.domain.model.core.Category

data class CategoryViewState(
    val isLoading: Boolean = false,
    val categories: List<Category> = listOf(),
    val error: String? = null
)
