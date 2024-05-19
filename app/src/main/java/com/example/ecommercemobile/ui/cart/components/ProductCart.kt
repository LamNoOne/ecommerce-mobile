package com.example.ecommercemobile.ui.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.ecommercemobile.store.domain.model.core.carts.ProductCart
import com.example.ecommercemobile.ui.components.CounterButton

@Composable
fun ProductCart(
    modifier: Modifier = Modifier,
    productCart: ProductCart
) {
    var checked by remember { mutableStateOf(false) }

    var valueCounter by remember {
        mutableStateOf(0)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.White)
            .clip(RoundedCornerShape(16.dp))
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Checkbox(checked = checked, onCheckedChange = { checked = it })
        AsyncImage(
            model = productCart.product.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .height(200.dp)
                .padding(6.dp)
                .aspectRatio(1f),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 6.dp, bottom = 6.dp, end = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = productCart.product.name,
                style = MaterialTheme.typography.titleSmall
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = productCart.product.price.toString(),
                    style = MaterialTheme.typography.bodySmall
                )
                CounterButton(
                    value = valueCounter.toString(),
                    onValueIncreaseClick = {
                        valueCounter = minOf(valueCounter + 1, 99)
                    },
                    onValueDecreaseClick = {
                        valueCounter = maxOf(valueCounter - 1, 0)
                    },
                    onValueClearClick = {
                        valueCounter = 0
                    },
                    modifier = Modifier
                        .width(80.dp)
                        .height(32.dp)
                )
            }
        }
    }
}