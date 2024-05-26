package com.selegend.ecommercemobile.ui.checkout.components

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
import com.selegend.ecommercemobile.store.domain.model.core.carts.ProductCart

@Composable
fun CheckoutProduct(checkoutProduct: ProductCart) {
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
                model = checkoutProduct.product.imageUrl,
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
                    text = checkoutProduct.product.name,
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
                        text = "$${checkoutProduct.product.price}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                    Text(
                        text = "x${checkoutProduct.quantity}",
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
                text = "Total payment (${checkoutProduct.quantity} item):",
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = " $${checkoutProduct.product.price * checkoutProduct.quantity}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Red
            )
        }
    }
}