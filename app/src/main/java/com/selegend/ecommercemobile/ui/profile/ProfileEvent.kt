package com.selegend.ecommercemobile.ui.profile

import android.content.Context
import android.net.Uri

sealed class ProfileEvent {

    data class OnProfileUpdate(val uri: Uri, val multipartName: String, val credentials: Map<String, String>, val context: Context) : ProfileEvent()

    data class OnProfileUpdateImage(val uri: Uri, val multipartName: String, val context: Context) : ProfileEvent()

    data class OnProfileUpdateCredentials(val credentials: Map<String, String>, val context: Context) : ProfileEvent()
}