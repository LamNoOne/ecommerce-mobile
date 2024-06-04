package com.selegend.ecommercemobile.ui.order_detail

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.selegend.ecommercemobile.ui.components.LoadingItem
import com.selegend.ecommercemobile.ui.order_detail.components.OrderCard
import com.selegend.ecommercemobile.ui.utils.UIEvent

@Composable
fun OrderDetailScreen(
    onPopBackStack: () -> Boolean,
    onNavigate: (UIEvent.Navigate) -> Unit,
    viewModel: OrderDetailViewModel = hiltViewModel()
) {

    val transactionState by viewModel.state.collectAsStateWithLifecycle()

    if (transactionState.isLoading == true) {
        LoadingItem()
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                androidx.compose.material.TopAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp,
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                            text = "Order Detail",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { Log.d("Search clicking...", "") }) {
                                Icon(
                                    imageVector = Icons.Outlined.Search,
                                    contentDescription = "Search Button"
                                )
                            }
                        }
                    }
                }
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    .padding(
                        top = it.calculateTopPadding(),
                        bottom = it.calculateBottomPadding(),
                        start = 16.dp,
                        end = 16.dp
                    )
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Order No.${transactionState.orders?.orderId}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.DarkGray
                            )
                            Text(
                                text = "${transactionState.orders?.createdAt}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.Gray
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextKeyValue(
                                key = "Tracking number:  ",
                                value = transactionState.orders?.transactionId?.uppercase() ?: "N/A"
                            )
                            Text(
                                text = "${transactionState.orders?.orderStatus}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = getColor("${transactionState.orders?.orderStatus}"),
                            )
                        }

                        transactionState.orders?.orderProducts?.forEach { orderProduct ->
                            OrderCard(
                                orderData = orderProduct
                            )
                        }
                        // Payment detail
                        HorizontalDivider(
                            thickness = 8.dp,
                            color = MaterialTheme.colorScheme.surfaceContainer
                        )
                        Text(
                            text = "Order Information",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 20.sp,
                            textAlign = TextAlign.Right,
                        )
                        HorizontalDivider(
                            thickness = 8.dp,
                            color = MaterialTheme.colorScheme.surfaceContainer
                        )
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            TextKeyValue(
                                key = "Name:  ",
                                value = "${transactionState.transaction?.customer?.firstName} ${transactionState.transaction?.customer?.lastName}"
                            )
                            TextKeyValue(
                                key = "Phone:  ",
                                value = "${transactionState.transaction?.customer?.phone}"
                            )
                            TextKeyValue(
                                key = "Payment method:  ",
                                value = "${transactionState.transaction?.cardType} **** **** **** ${transactionState.transaction?.last4}"
                            )
                            TextKeyValue(
                                key = "Shipping Address:  ",
                                value = "${transactionState.transaction?.shipping?.streetAddress}, ${transactionState.transaction?.shipping?.region}, ${transactionState.transaction?.shipping?.postalCode}, ${transactionState.transaction?.shipping?.postalCode}"
                            )
                            TextKeyValue(
                                key = "Total Amount:  ",
                                value = "$${transactionState.orders?.totalAmount}"
                            )
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun TextKeyValue(
    key: String,
    value: String,
) {
    androidx.compose.material.Text(buildAnnotatedString {
        withStyle(style = ParagraphStyle(lineHeight = 20.sp)) {
            withStyle(
                style = SpanStyle(
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                )
            ) {
                append(key)
            }
            withStyle(
                style = SpanStyle(
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                )
            ) {
                append(value)
            }
        }
    })
}

private fun getColor(status: String): Color {
    return when (status) {
        "Delivered" -> Color(0xFF2AA952)
        "Paid" -> Color(0xFF2AA952)
        "Delivering" -> Color(0xFFE9A115)
        "Pending" -> Color(0xFFE9A115)
        else -> Color.Transparent
    }
}