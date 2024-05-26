@file:OptIn(ExperimentalMaterial3Api::class)

package com.selegend.ecommercemobile.ui.checkout

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.selegend.ecommercemobile.store.domain.model.core.checkout.CheckoutFromCart
import com.selegend.ecommercemobile.store.domain.model.core.payment.ProductPayment
import com.selegend.ecommercemobile.ui.checkout.components.CheckoutProduct
import com.selegend.ecommercemobile.ui.components.LoadingItem
import com.selegend.ecommercemobile.ui.utils.UIEvent
import kotlinx.coroutines.launch

@Composable
fun CheckoutScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UIEvent.Navigate) -> Unit,
    viewModel: CheckoutViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
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
    Log.d("CheckoutScreen", "state: $state")
    var isSheetOpen by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var showDialog by remember { mutableStateOf(false) }
    var address by remember {
        mutableStateOf<String>(viewModel.authState?.address ?: "")
    }

    val total = state.cart?.products?.sumOf<ProductPayment> { it.product.price * it.quantity } ?: 0

    var phoneNumber by remember {
        mutableStateOf<String>(viewModel.authState?.phoneNumber ?: "")
    }

    fun showDialogHandler(showDialogStatus: Boolean) {
        showDialog = showDialogStatus
    }

    fun removeAddress() {
        address = ""
        phoneNumber = ""
    }

    ConfirmDialog(
        showDialog,
        showDialogHandler = { showDialogHandler(it) },
        removeHandler = { removeAddress() })

    if (state.isLoading) {
        LoadingItem()
    } else {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
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
                                    contentDescription = "Back"
                                )
                            }
                            Text(
                                text = "Payment",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                        }
                    }
                }
            },
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Total payment:",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.DarkGray,
                            )
                            Text(
                                text = "$$total",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Red
                            )
                        }
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = {
                                    viewModel.onEvent(CheckoutEvent.OnCreateOrder(
                                        CheckoutFromCart(
                                            shipAddress = address,
                                            phoneNumber = phoneNumber,
                                            paymentFormId = 1,
                                            orderProducts = state.cart?.products?.map { it.product.id }
                                                ?: emptyList()
                                            )
                                        )
                                    )
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(0.dp),
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(130.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Place order",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        ) { it ->
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
                        if (address.isEmpty() && phoneNumber.isEmpty()) {
                            Button(
                                onClick = { isSheetOpen = true },
                                shape = RoundedCornerShape(0.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Add new ship address",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.DarkGray,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        } else {
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
                                        Text(
                                            text = "Edit",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Normal,
                                            modifier = Modifier
                                                .clickable {
                                                    Log.d(
                                                        "CheckoutScreen",
                                                        "Edit address clicking....."
                                                    )
                                                    isSheetOpen = true
                                                }
                                        )
                                    }
                                    Text(
                                        text = "${viewModel.authState?.lastName} ${viewModel.authState?. firstName} | $phoneNumber" +
                                                "\n$address",
                                        fontSize = 12.sp,
                                        color = Color.DarkGray,
                                        fontWeight = FontWeight.Normal,
                                        maxLines = 3,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                        state.cart?.products?.forEach { productCart ->
                            CheckoutProduct(
                                checkoutProduct = productCart
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
                                    text = " $$total",
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
                                    text = " $$total",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Red
                                )
                            }
                        }
                        if (isSheetOpen) {
                            ModalBottomSheet(
                                sheetState = sheetState,
                                onDismissRequest = {
                                    isSheetOpen = false
                                },
                                containerColor = Color.White,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(400.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.surfaceContainer)
                                            .padding(horizontal = 16.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Contact",
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 12.sp,
                                            color = Color.DarkGray
                                        )
                                    }
                                    HorizontalDivider(
                                        thickness = 1.dp,
                                        color = MaterialTheme.colorScheme.surfaceContainer
                                    )
                                    TextField(
                                        value = phoneNumber,
                                        textStyle = TextStyle(
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Normal,
                                            color = Color.DarkGray
                                        ),
                                        colors = TextFieldDefaults.colors(
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            focusedContainerColor = Color.Transparent,
                                            unfocusedContainerColor = Color.Transparent
                                        ),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        onValueChange = {
                                            phoneNumber = it
                                        },
                                        placeholder = {
                                            Text(text = "Phone number")
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.surfaceContainer)
                                            .padding(horizontal = 16.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Address",
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 12.sp,
                                            color = Color.DarkGray
                                        )
                                    }
                                    TextField(
                                        value = address,
                                        textStyle = TextStyle(
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Normal,
                                            color = Color.DarkGray
                                        ),
                                        colors = TextFieldDefaults.colors(
                                            focusedIndicatorColor = Color.Transparent,
                                            unfocusedIndicatorColor = Color.Transparent,
                                            focusedContainerColor = Color.Transparent,
                                            unfocusedContainerColor = Color.Transparent
                                        ),
                                        onValueChange = {
                                            address = it
                                        },
                                        placeholder = {
                                            Text(text = "Ship address")
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    HorizontalDivider(
                                        thickness = 8.dp,
                                        color = MaterialTheme.colorScheme.surfaceContainer
                                    )
                                    Button(
                                        onClick = {
                                            showDialog = true
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color.White
                                        ),
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(0.dp))
                                            .fillMaxWidth()
                                            .height(56.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxSize(),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "Remove address",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = MaterialTheme.colorScheme.primary,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                    HorizontalDivider(
                                        thickness = 8.dp,
                                        color = MaterialTheme.colorScheme.surfaceContainer
                                    )
                                    Button(
                                        onClick = { isSheetOpen = false },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(56.dp)
                                            .clip(RoundedCornerShape(0.dp))
                                            .background(MaterialTheme.colorScheme.primary)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxSize(),
                                            horizontalArrangement = Arrangement.Center,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "DONE",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Medium,
                                                color = Color.White,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConfirmDialog(
    showDialog: Boolean,
    showDialogHandler: (Boolean) -> Unit,
    removeHandler: () -> Unit
) {

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
                    removeHandler()
                    showDialogHandler(false)
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


