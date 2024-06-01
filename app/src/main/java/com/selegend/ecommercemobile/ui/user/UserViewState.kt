package com.selegend.ecommercemobile.ui.user

import com.selegend.ecommercemobile.store.domain.model.core.users.User

data class UserViewState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
)