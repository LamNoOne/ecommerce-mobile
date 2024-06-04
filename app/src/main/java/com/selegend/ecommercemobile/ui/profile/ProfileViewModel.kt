package com.selegend.ecommercemobile.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selegend.ecommercemobile.store.domain.model.core.auth.Auth
import com.selegend.ecommercemobile.store.domain.repository.AuthRepository
import com.selegend.ecommercemobile.store.domain.repository.UserRepository
import com.selegend.ecommercemobile.ui.user.UserViewState
import com.selegend.ecommercemobile.ui.utils.UIEvent
import com.selegend.ecommercemobile.utils.Event
import com.selegend.ecommercemobile.utils.EventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private var auth by mutableStateOf<Auth?>(null)
    private var _state = MutableStateFlow(UserViewState())
    val state = _state.asStateFlow()

    /**
     * Channel for UI events.
     */
    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            authRepository.getAuth(1)?.let {
                auth = it
            }
        }
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            userRepository.getUserInfo(headers = getHeaderMap())
                .onRight { response ->
                    _state.update { it.copy(user = response.metadata.user) }
                }
                .onLeft { error ->
                    _state.update { it.copy(error = error.error.message) }
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    @SuppressLint("Recycle")
    private fun createMultipartBody(
        uri: Uri,
        multipartName: String,
        context: Context
    ): MultipartBody.Part {
        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")
        val inputStream = FileInputStream(parcelFileDescriptor?.fileDescriptor)
        val file = File(context.cacheDir, multipartName)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(name = multipartName, file.name, requestBody)
    }

    private fun createPartMapFromString(stringData: String): RequestBody {
        return stringData.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun updateUser(
        multipartBody: MultipartBody.Part?,
        partMap: MutableMap<String, RequestBody>?
    ) {
        viewModelScope.launch {
            if (multipartBody == null && partMap == null) {
                EventBus.sendEvent(Event.Toast("No data to update"))
                return@launch
            }
            if (multipartBody != null && partMap != null) {
                userRepository.updateUserInfo(getHeaderMap(), multipartBody, partMap)
                    .onRight { response ->
                        if (response.statusCode == 200 || response.statusCode == 201) {
                            EventBus.sendEvent(Event.Toast("Profile updated successfully"))
                        }
                    }.onLeft {
                        EventBus.sendEvent(Event.Toast("Profile update failed"))
                    }
                return@launch
            }
            if (multipartBody != null && partMap == null) {
                userRepository.updateUserImage(getHeaderMap(), multipartBody)
                    .onRight { response ->
                        if (response.statusCode == 200 || response.statusCode == 201) {
                            EventBus.sendEvent(Event.Toast("Profile updated successfully"))
                        }
                    }.onLeft {
                        EventBus.sendEvent(Event.Toast("Profile update failed"))
                    }
                return@launch
            }
            if (partMap != null) {
                userRepository.updateUserCredentials(getHeaderMap(), partMap)
                    .onRight { response ->
                        if (response.statusCode == 200 || response.statusCode == 201) {
                            EventBus.sendEvent(Event.Toast("Profile updated successfully"))
                        }
                    }.onLeft {
                        EventBus.sendEvent(Event.Toast("Profile update failed"))
                    }
                return@launch
            }
            return@launch
        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.OnProfileUpdate -> {
                viewModelScope.launch {
                    val image = createMultipartBody(event.uri, event.multipartName, event.context)
                    val credentials = event.credentials
                    val partMap = mutableMapOf<String, RequestBody>()
                    credentials.forEach { (key, value) ->
                        if (value.isNotBlank()) {
                            partMap[key] = createPartMapFromString(value)
                        }
                    }
                    updateUser(image, partMap)
                }
            }
            is ProfileEvent.OnProfileUpdateImage -> {
                viewModelScope.launch {
                    val image = createMultipartBody(event.uri, event.multipartName, event.context)
                    updateUser(image, null)
                }
            }
            is ProfileEvent.OnProfileUpdateCredentials -> {
                viewModelScope.launch {
                    val credentials = event.credentials
                    val partMap = mutableMapOf<String, RequestBody>()
                    credentials.forEach { (key, value) ->
                        if (value.isNotBlank()) {
                            partMap[key] = createPartMapFromString(value)
                        }
                    }
                    updateUser(null, partMap)
                }
            }
        }
    }

    private fun getHeaderMap(): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["Authorization"] = "Bearer ${auth?.accessToken}"
        headerMap["x-user-id"] = auth?.userId.toString()
        return headerMap
    }

    private fun sendUIEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}

