package com.selegend.ecommercemobile.ui.user

import com.selegend.ecommercemobile.store.domain.model.core.auth.Auth

sealed class UserEvent {
    object OnCartClick: UserEvent()
    object OnEditProfileClick: UserEvent()
    object OnOrderHistoryClick: UserEvent()
    object OnFavoriteClick: UserEvent()
    object OnSettingsClick: UserEvent()
    data class OnLogoutClick(val auth: Auth): UserEvent()
}