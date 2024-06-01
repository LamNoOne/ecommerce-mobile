package com.selegend.ecommercemobile.ui.order_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.selegend.ecommercemobile.store.domain.model.core.auth.Auth
import com.selegend.ecommercemobile.store.domain.repository.AuthRepository
import com.selegend.ecommercemobile.store.domain.repository.TransactionRepository
import com.selegend.ecommercemobile.ui.utils.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class OrderDetailViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var auth by mutableStateOf<Auth?>(null)
    private var _state = MutableStateFlow(OrderDetailViewState())
    val state = _state.asStateFlow()

    /**
     * Channel for UI events.
     */
    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        runBlocking {
            authRepository.getAuth(1)?.let {
                auth = it
            }
        }

        val transactionId = savedStateHandle.get<String>("transactionId")

        // call api to get transaction detail
        transactionId?.let {
            getTransactionDetail(it)
        }
    }

    fun getTransactionDetail(transactionId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            transactionRepository.getTransaction(getHeaderMap(), transactionId)
                .onRight { response ->
                    _state.update {
                        it.copy(
                            transaction = response.metadata.transaction,
                            orders = response.metadata.order
                        )
                    }
                }
                .onLeft { error ->
                    _state.update { it.copy(error = error.error.message) }
                }
            _state.update { it.copy(isLoading = false) }
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