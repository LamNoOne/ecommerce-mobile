package com.example.ecommercemobile.ui.home.events

sealed class CategoryEvent {
    data class OnCategoryClick(val categoryId: Int): CategoryEvent()
}