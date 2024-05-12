package com.example.ecommercemobile.ui.home.events

import android.icu.text.StringSearch

sealed class CoreEvent {
    object OnHomeClick : CoreEvent()
    object OnFavoriteClick : CoreEvent()
    object OnCartClick : CoreEvent()
    object OnProfileClick : CoreEvent()
    object OnLoginClick : CoreEvent()
    data class OnSearchClick(val search: StringSearch) : CoreEvent()
}