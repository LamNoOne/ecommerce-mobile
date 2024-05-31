package com.selegend.ecommercemobile.ui.orders

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.selegend.ecommercemobile.store.domain.model.core.auth.Auth
import com.selegend.ecommercemobile.store.domain.repository.AuthRepository
import com.selegend.ecommercemobile.store.domain.repository.CheckoutRepository
import com.selegend.ecommercemobile.ui.utils.UIEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val checkoutRepository: CheckoutRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private var auth by mutableStateOf<Auth?>(null)
    private var _allOrder = MutableStateFlow(OrdersViewState())
    private var _pendingOrder = MutableStateFlow(OrdersViewState())
    private var _paidOrder = MutableStateFlow(OrdersViewState())
    private var _deliveringOrder = MutableStateFlow(OrdersViewState())
    private var _deliveredOrder = MutableStateFlow(OrdersViewState())

    val allOrder = _allOrder.asStateFlow()
    val pendingOrder = _pendingOrder.asStateFlow()
    val paidOrder = _paidOrder.asStateFlow()
    val deliveringOrder = _deliveringOrder.asStateFlow()
    val deliveredOrder = _deliveredOrder.asStateFlow()

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

    }

    private val allOrderParam =
        OrdersParams(status = OrderStatus.DEFAULT.value, headers = getHeaderMap())
    private val pendingOrderParam =
        OrdersParams(status = OrderStatus.PENDING.value, headers = getHeaderMap())
    private val paidOrderParam =
        OrdersParams(status = OrderStatus.PAID.value, headers = getHeaderMap())
    private val deliveringOrderParam =
        OrdersParams(status = OrderStatus.DELIVERING.value, headers = getHeaderMap())
    private val deliveredOrderParam =
        OrdersParams(status = OrderStatus.DELIVERED.value, headers = getHeaderMap())

    val allOrderPager = Pager(PagingConfig(pageSize = 20)) {
        OrdersDataSource(checkoutRepository, allOrderParam)
    }.flow.cachedIn(viewModelScope)

    val pendingOrderPager = Pager(PagingConfig(pageSize = 20)) {
        OrdersDataSource(checkoutRepository, pendingOrderParam)
    }.flow.cachedIn(viewModelScope)

    val paidOrderPager = Pager(PagingConfig(pageSize = 20)) {
        OrdersDataSource(checkoutRepository, paidOrderParam)
    }.flow.cachedIn(viewModelScope)

    val deliveringOrderPager = Pager(PagingConfig(pageSize = 20)) {
        OrdersDataSource(checkoutRepository, deliveringOrderParam)
    }.flow.cachedIn(viewModelScope)

    val deliveredOrderPager = Pager(PagingConfig(pageSize = 20)) {
        OrdersDataSource(checkoutRepository, deliveredOrderParam)
    }.flow.cachedIn(viewModelScope)

    fun onEvent(event: OrdersEvent) {
        when (event) {
            is OrdersEvent.OnOrderClick -> {
//                sendUIEvent(UIEvent.Navigate("${Routes.ORDER_DETAIL}?orderId=${event.orderId}"))
            }
            is OrdersEvent.OnSearchClick -> {
//                sendUIEvent(UIEvent.Navigate("${Routes.ORDER_DETAIL}?orderId=${event.orderId}"))
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

data class OrdersParams(
    val headers: Map<String, String>,
    val limit: Int = 20,
    val sortBy: String = SORT_BY_DEFAULT,
    val order: String = Order.DESC.value,
    val status: String,
    val name: String = ""
)

enum class Order(val value: String) {
    ASC("asc"),
    DESC("desc"),
    DEFAULT("")
}

enum class OrderStatus(val value: String) {
    PENDING("Pending"),
    PAID("Paid"),
    DELIVERING("Delivering"),
    DELIVERED("Delivered"),
    DEFAULT("")
}

const val SORT_BY_DEFAULT = "createdAt"