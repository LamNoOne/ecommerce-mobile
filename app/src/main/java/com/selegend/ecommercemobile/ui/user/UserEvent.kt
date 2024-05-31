package com.selegend.ecommercemobile.ui.user

sealed class UserEvent {
    object OnCartClick: UserEvent()
    object OnEditProfileClick: UserEvent()
    object OnOrderHistoryClick: UserEvent()
    object OnFavoriteClick: UserEvent()
    object OnSettingsClick: UserEvent()
}