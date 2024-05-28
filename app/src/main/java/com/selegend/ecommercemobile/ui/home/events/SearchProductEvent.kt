package com.selegend.ecommercemobile.ui.home.events

sealed class SearchProductEvent {
    data class OnProductClick(val productId: Int): SearchProductEvent()
    data class OnSubmitSearch(val search: String): SearchProductEvent()
    data class OnSearchQueryChange(val query: String): SearchProductEvent()
}