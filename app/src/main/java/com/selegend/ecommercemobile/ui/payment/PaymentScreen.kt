package com.selegend.ecommercemobile.ui.payment

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.samples.pay.util.PaymentsUtil
import com.google.pay.button.ButtonTheme
import com.google.pay.button.ButtonType
import com.google.pay.button.PayButton
import com.selegend.ecommercemobile.R
import com.selegend.ecommercemobile.ui.checkout.components.CheckoutProduct
import com.selegend.ecommercemobile.ui.utils.UIEvent
import kotlinx.coroutines.launch

@Composable
fun PaymentScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UIEvent.Navigate) -> Unit,
    onGooglePayButtonClick: () -> Unit,
    payUiState: PaymentUiState = PaymentUiState.NotStarted,
    setOrderId: (Int) -> Unit,
    transactionState: TransactionViewState,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Log.d("PaymentScreen", "state: ${transactionState.transactionId}:::${transactionState.success}")
    setOrderId(state.order?.orderId ?: 0)
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.ShowSnackBar -> {
                    scope.launch {
                        scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = event.action,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
                is UIEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }
    Log.d("PaymentScreen", "c: $payUiState")

    if (transactionState.success && transactionState.orderId == state.order?.orderId) {
        Log.d("PaymentScreen Transaction ID", transactionState.transactionId.toString())
        Column(
            modifier = Modifier
                .testTag("successScreen")
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                contentDescription = null,
                painter = painterResource(R.drawable.check_circle),
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "You completed a payment.\nWe are preparing your order.",
                fontSize = 17.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.height(60.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "VIEW ORDER",
                        fontSize = 17.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "OR",
                fontSize = 17.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Button(
                onClick = { viewModel.onEvent(PaymentEvent.OnClickContinue) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.height(60.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CONTINUE SHOPPING",
                        fontSize = 17.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            topBar = {
                androidx.compose.material.TopAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp,
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    onPopBackStack()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBackIosNew,
                                    contentDescription = "Payment"
                                )
                            }
                            Text(
                                text = "Payment Information",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                        }
                    }
                }
            }, bottomBar = {
                BottomAppBar(
                    modifier = Modifier
                        .fillMaxWidth(),
                    containerColor = Color.White
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Settle google pay here
                        if (payUiState !is PaymentUiState.NotStarted) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                            ) {
                                Text(
                                    text = "Total payment: ",
                                    color = Color.DarkGray,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal
                                )
                                Text(
                                    text = "$${state.order?.totalAmount}",
                                    color = Color.Red,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            PayButton(
                                modifier = Modifier
                                    .testTag("payButton")
                                    .fillMaxSize()
                                    .padding(bottom = 6.dp),
                                onClick = onGooglePayButtonClick,
                                allowedPaymentMethods = PaymentsUtil.allowedPaymentMethods.toString(),
                                radius = 0.dp,
                                type = ButtonType.Pay,
                                theme = ButtonTheme.Dark
                            )
                        }
                    }
                }
            }
        ) {
            LazyVerticalStaggeredGrid(
                modifier = Modifier.padding(
                    top = it.calculateTopPadding(),
                    bottom = it.calculateBottomPadding()
                ),
                columns = StaggeredGridCells.Fixed(1),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalItemSpacing = 10.dp
            ) {
                item {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocalShipping,
                                contentDescription = "Local shipping"
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 28.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Ship address",
                                        fontSize = 12.sp,
                                        color = Color.DarkGray,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                                Text(
                                    text = "1234 Main St, New York, NY 10001",
                                    fontSize = 12.sp,
                                    color = Color.DarkGray,
                                    fontWeight = FontWeight.Normal,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                        state.order?.orderProducts?.forEach { orderProduct ->
                            CheckoutProduct(
                                checkoutProduct = orderProduct
                            )
                        }
                        // Payment detail
                        HorizontalDivider(
                            thickness = 8.dp,
                            color = MaterialTheme.colorScheme.surfaceContainer
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Payment,
                                    contentDescription = "Payment detail"
                                )
                                Text(
                                    text = "Payment detail",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.DarkGray
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Subtotal",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.DarkGray
                                )
                                Text(
                                    text = "$${state.order?.totalAmount}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.DarkGray
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Shipping",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.DarkGray
                                )
                                Text(
                                    text = " $0",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.DarkGray
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.DarkGray
                                )
                                Text(
                                    text = "$${state.order?.totalAmount}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Red
                                )
                            }
                        }
                        HorizontalDivider(
                            modifier = Modifier.height(24.dp)
                        )
                        Button(
                            onClick = { /*TODO*/ },
                            shape = RoundedCornerShape(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            border = BorderStroke(1.dp, Color.DarkGray),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Confirm cancel",
                                    color = Color.DarkGray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConfirmDialogOrder(
    showDialog: Boolean,
    showDialogHandler: (Boolean) -> Unit,
    showProgressHandler: (Boolean) -> Unit,
    onPopBackStack: () -> Unit,
    viewModel: PaymentViewModel
) {
    /**
     * 1. If user click cancel order
     * 2. Show dialog to confirm cancel order
     * 3. If user click yes, remove order: turn of dialog, show circular progress -> set show state, send event to view model
     * 4. If user click no, turn of dialog
     */

    if (showDialog) {
        androidx.compose.material.AlertDialog(
            onDismissRequest = { showDialogHandler(false) },
            backgroundColor = Color.White,
            text = { Text("Do you want to remove address?") },
            dismissButton = {
                androidx.compose.material.TextButton(onClick = { showDialogHandler(false) }) {
                    Text(
                        text = "Cancel",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = androidx.compose.material.MaterialTheme.colors.primaryVariant
                    )
                }
            },
            confirmButton = {
                androidx.compose.material.TextButton(onClick = {
                    showDialogHandler(false)
                    showProgressHandler(true)
//                    viewModel.onEvent(PaymentEvent.RemoveOrder)
                    // check state is emptu
                    showProgressHandler(false)
//                    onPopBackStack() I think we should remove all stack because the previous screen is not valid
                    // navigate to home screen
                }) {
                    Text(
                        text = "Yes",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = androidx.compose.material.MaterialTheme.colors.primaryVariant
                    )
                }
            }
        )
    }
}

/**
 * Actually, we don't need to pass all these parameters to the composable function.
 * Just create a function to handle all of that logic
 * and pass only it to the composable function,
 * when user click on the button, call that function
 */