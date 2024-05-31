package com.selegend.ecommercemobile.ui.payment

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.samples.pay.util.PaymentsUtil
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.google.android.gms.wallet.IsReadyToPayRequest
import com.google.android.gms.wallet.PaymentData
import com.google.android.gms.wallet.PaymentDataRequest
import com.google.android.gms.wallet.PaymentsClient
import com.google.gson.Gson
import com.selegend.ecommercemobile.store.domain.model.GooglePayJson
import com.selegend.ecommercemobile.store.domain.model.core.auth.Auth
import com.selegend.ecommercemobile.store.domain.model.core.transaction.MakeTransaction
import com.selegend.ecommercemobile.store.domain.repository.AuthRepository
import com.selegend.ecommercemobile.store.domain.repository.CheckoutRepository
import com.selegend.ecommercemobile.store.domain.repository.TransactionRepository
import com.selegend.ecommercemobile.ui.utils.UIEvent
import com.selegend.ecommercemobile.utils.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import org.json.JSONException
import org.json.JSONObject
import java.util.concurrent.Executor
import javax.inject.Inject
import kotlin.coroutines.resume

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val checkoutRepository: CheckoutRepository,
    private val transactionRepository: TransactionRepository,
    application: Application,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    // set mutable state
    private var _state = MutableStateFlow(PaymentViewState())

    // get immutable one
    val state = _state.asStateFlow()


    // make a transaction to server and receive a transaction id
    private var _transactionState = MutableStateFlow(TransactionViewState(success = false))
    val transactionState = _transactionState.asStateFlow()

    private var auth by mutableStateOf<Auth?>(null)

    /**
     * Channel for UI events.
     */
    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _paymentUiState: MutableStateFlow<PaymentUiState> =
        MutableStateFlow(PaymentUiState.NotStarted)
    val paymentUiState: StateFlow<PaymentUiState> = _paymentUiState.asStateFlow()

    // A client for interacting with the Google Pay API.
    private val paymentsClient: PaymentsClient = PaymentsUtil.createPaymentsClient(application)

    init {
        runBlocking {
            authRepository.getAuth(1)?.let {
                auth = it
            }
        }

        viewModelScope.launch {
            verifyGooglePayReadiness()
        }

        val orderId = savedStateHandle.get<Int>("orderId")
        orderId?.let {
            getOrder(it)
        }
    }

    private fun getOrder(orderId: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            checkoutRepository.getCheckoutOrder(getHeaderMap(), orderId)
                .onRight { orderResponse ->
                    _state.update { currentState ->
                        currentState.copy(order = orderResponse.metadata.order)
                    }
                }
                .onLeft { err ->
                    _state.update {
                        it.copy(error = err.error.message)
                    }
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun onEvent(event: PaymentEvent) {
        when (event) {
            is PaymentEvent.OnClickContinue -> {
                sendUIEvent(UIEvent.Navigate(Routes.HOME))
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

    /**
     * Google Payment
     */

    /**
     * Determine the user's ability to pay with a payment method supported by your app and display
     * a Google Pay payment button.
    ) */
    private suspend fun verifyGooglePayReadiness() {
        val newUiState: PaymentUiState = try {
            if (fetchCanUseGooglePay()) {
                PaymentUiState.Available
            } else {
                PaymentUiState.Error(CommonStatusCodes.ERROR)
            }
        } catch (exception: ApiException) {
            PaymentUiState.Error(exception.statusCode, exception.message)
        }

        _paymentUiState.update { newUiState }
    }

    /**
     * Determine the user's ability to pay with a payment method supported by your app.
    ) */
    private suspend fun fetchCanUseGooglePay(): Boolean {
        val request = IsReadyToPayRequest.fromJson(PaymentsUtil.isReadyToPayRequest().toString())
        return paymentsClient.isReadyToPay(request).await()
    }

    /**
     * Creates a [Task] that starts the payment process with the transaction details included.
     *
     * @return a [Task] with the payment information.
     * @see [PaymentDataRequest](https://developers.google.com/android/reference/com/google/android/gms/wallet/PaymentsClient#loadPaymentData(com.google.android.gms.wallet.PaymentDataRequest)
    ) */
    fun getLoadPaymentDataTask(priceCents: Long): Task<PaymentData> {
        val paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(priceCents)
        val request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString())
        return paymentsClient.loadPaymentData(request)
    }

    /**
     * At this stage, the user has already seen a popup informing them an error occurred. Normally,
     * only logging is required.
     *
     * @param statusCode will hold the value of any constant from CommonStatusCode or one of the
     * WalletConstants.ERROR_CODE_* constants.
     * @see [
     * Wallet Constants Library](https://developers.google.com/android/reference/com/google/android/gms/wallet/WalletConstants.constant-summary)
     */
    private fun handleError(statusCode: Int, message: String?) {
        Log.e("Google Pay API error", "Error code: $statusCode, Message: $message")
    }

    fun setPaymentData(paymentData: PaymentData, orderId: Int) {
        val payState = extractPaymentBillingName(paymentData, orderId)?.let {
            PaymentUiState.PaymentCompleted(transactionId = it)
        } ?: PaymentUiState.Error(CommonStatusCodes.INTERNAL_ERROR)

        _paymentUiState.update { payState }
    }

    private suspend fun createTransaction(orderId: Int, nonce: String) {
        transactionRepository.createTransaction(
            getHeaderMap(),
            MakeTransaction(orderId, nonce)
        )
            .onRight { transactionResponse ->
                _transactionState.update { currentState ->
                    currentState.copy(
                        transactionId = transactionResponse.metadata.transaction.id,
                        orderId = orderId,
                        success = transactionResponse.metadata.success
                    )
                }
            }
            .onLeft { err ->
                Log.d("Error", err.error.message)
            }
    }

    private fun extractPaymentBillingName(paymentData: PaymentData, orderId: Int): String? {
        val paymentInformation = paymentData.toJson()

        try {
            // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
            val paymentMethodData =
                JSONObject(paymentInformation).getJSONObject("paymentMethodData")
//            val shipAddress = paymentMethodData.getJSONObject("info")
//                .getJSONObject("billingAddress")
//            Log.d("ShippingAddress", shipAddress.toString())
//            val billingName = paymentMethodData.getJSONObject("info")
//                .getJSONObject("billingAddress").getString("name")
//            Log.d("BillingName", billingName)

            Log.d("ORDERIDD:::::", orderId.toString())
            Log.d("PaymentMethodData", paymentMethodData.toString())

            // Logging token string.
            val paymentJson = paymentMethodData.getJSONObject("tokenizationData").getString("token")


            val gson = Gson()
            val response = gson.fromJson(paymentJson, GooglePayJson::class.java)
            val nonce = response.androidPayCards[0].nonce
//            val orderId = _state.value.order?.orderId ?: 0
            Log.d("Nonce", nonce)
            Log.d("OrderId", orderId.toString())

            runBlocking {
                if (orderId != null) {
                    createTransaction(orderId, nonce)
                }
            }

            Log.d("TransactionState", transactionState.value.toString())

            if (transactionState.value.success) {
                return transactionState.value.transactionId
            }
            return null
        } catch (error: JSONException) {
            Log.e("Error", error.message.toString())
        }

        return null
    }
}


abstract class PaymentUiState internal constructor() {
    object NotStarted : PaymentUiState()
    object Available : PaymentUiState()
    class PaymentCompleted(val transactionId: String) : PaymentUiState()
    class Error(val code: Int, val message: String? = null) : PaymentUiState()
}

suspend fun <T> Task<T>.awaitTask(cancellationTokenSource: CancellationTokenSource? = null): Task<T> {
    return if (isComplete) this else suspendCancellableCoroutine { cont ->
        // Run the callback directly to avoid unnecessarily scheduling on the main thread.
        addOnCompleteListener(DirectExecutor, cont::resume)

        cancellationTokenSource?.let { cancellationSource ->
            cont.invokeOnCancellation { cancellationSource.cancel() }
        }
    }
}

/**
 * An [Executor] that just directly executes the [Runnable].
 */
private object DirectExecutor : Executor {
    override fun execute(r: Runnable) {
        r.run()
    }
}
