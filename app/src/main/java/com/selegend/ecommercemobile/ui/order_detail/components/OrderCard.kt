package com.selegend.ecommercemobile.ui.order_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.selegend.ecommercemobile.store.domain.model.core.orders.OrderProduct

@Composable
fun OrderCard(orderData: OrderProduct) {
    HorizontalDivider(
        thickness = 8.dp,
        color = MaterialTheme.colorScheme.surfaceContainer
    )
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
        ) {
            AsyncImage(
                model = orderData.product.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .height(100.dp)
                    .padding(6.dp)
                    .aspectRatio(1f),
                contentScale = ContentScale.FillBounds
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = orderData.product.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(intrinsicSize = IntrinsicSize.Min),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$${orderData.product.price}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                    Text(
                        text = "x${orderData.quantity}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                }
            }
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.surfaceContainer
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.White)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total payment (${orderData.quantity} item):",
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = " $${orderData.product.price * orderData.quantity}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Red
            )
        }
    }
}