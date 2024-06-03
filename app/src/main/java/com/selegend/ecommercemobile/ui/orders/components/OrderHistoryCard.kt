package com.selegend.ecommercemobile.ui.orders.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.selegend.ecommercemobile.store.domain.model.core.orders.OrderHistory
import com.selegend.ecommercemobile.ui.orders.OrdersEvent
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrderHistoryCard(
    modifier: Modifier = Modifier,
    orderState: OrderHistory,
    onEvent: (OrdersEvent) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Order No.${orderState.orderId}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${Instant.parse(orderState.createdAt)}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tracking number:",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            )
            Text(
                text = orderState.transactionId?.uppercase() ?: "N/A",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextOrderQuantityAmount(
                title = "Quantity:  ",
                amount = "${orderState.orderProducts.size}"
            )
            TextOrderQuantityAmount(
                title = "Total Amount:  ",
                amount = "$${orderState.totalAmount}"
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .width(100.dp)
                    .height(40.dp),
                shape = RoundedCornerShape(24.dp),
                border = ButtonDefaults.outlinedButtonBorder,
                onClick = {
                    if (orderState.transactionId != null) {
                        onEvent(
                            OrdersEvent.OnOrderClick(transactionId = orderState.transactionId)
                        )
                    } else {
                        onEvent(OrdersEvent.OnPaymentClick(orderId = orderState.orderId))
                    }
                })
            {
                Text(
                    text = if(orderState.orderStatus == "Pending") "Pay" else "Details",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                )
            }
            Text(
                text = orderState.orderStatus,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = getColor(orderState.orderStatus)
            )
        }
    }
}


@Composable
private fun TextOrderQuantityAmount(
    title: String,
    amount: String,
) {
    androidx.compose.material.Text(buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        ) {
            append(title)
        }
        withStyle(
            style = SpanStyle(
                color = Color.DarkGray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
            )
        ) {
            append(amount)
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

enum class OrderStatus(val status: String) {
    DELIVERED("Delivered"),
    PAID("Paid"),
    DELIVERING("Delivering"),
    PENDING("Pending")
}