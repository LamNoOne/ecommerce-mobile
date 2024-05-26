package com.selegend.ecommercemobile.ui.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.selegend.ecommercemobile.store.domain.model.core.payment.ProductPayment
import com.selegend.ecommercemobile.ui.cart.CartEvent
import com.selegend.ecommercemobile.ui.components.CounterButton

@Composable
fun ProductCart(
    modifier: Modifier = Modifier,
    onPopBackStack: () -> Unit,
    onEvent: (CartEvent) -> Unit,
    itemChecked: Boolean,
    updateAllChecked: (Boolean) -> Unit,
    updateItemChecked: (Boolean) -> Unit,
    updateSelectedItem: (ProductPayment) -> Unit,
    deleteSelectedItem: (ProductPayment) -> Unit,
    productCart: ProductPayment
) {

    var valueCounter by remember {
        mutableStateOf(productCart.quantity)
    }

    fun updateValueCounter(value: Int) {
        valueCounter = value
    }

    ConfirmDialog(
        onPopBackStack = onPopBackStack,
        onEvent = onEvent,
        changeQuantity = {
            updateValueCounter(it)
        },
        productId = productCart.product.id,
        quantity = valueCounter
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(Color.White)
            .clip(RoundedCornerShape(16.dp))
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Checkbox(checked = itemChecked, onCheckedChange = {
            updateItemChecked(it)
            if (it) {
                updateSelectedItem(productCart)
            } else {
                updateAllChecked(false)
                deleteSelectedItem(productCart)
            }
        })
        AsyncImage(
            model = productCart.product.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .height(240.dp)
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
                verticalAlignment = Alignment.Bottom
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "$${productCart.product.price}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Red,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "$${productCart.product.price + 50}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        textDecoration = TextDecoration.LineThrough
                    )
                }
                CounterButton(
                    value = valueCounter.toString(),
                    onValueIncreaseClick = {
                        valueCounter = minOf(valueCounter + 1, 99)
                        onEvent(
                            CartEvent.OnChangeProductQuantity(
                                productCart.product.id,
                                valueCounter
                            )
                        )
                    },
                    onValueDecreaseClick = {
                        valueCounter = maxOf(valueCounter - 1, 0)
                        onEvent(
                            CartEvent.OnChangeProductQuantity(
                                productCart.product.id,
                                valueCounter
                            )
                        )
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

@Composable
private fun ConfirmDialog(
    onPopBackStack: () -> Unit,
    changeQuantity: (Int) -> Unit,
    onEvent: (CartEvent) -> Unit,
    productId: Int,
    quantity: Int
) {
    var showDialog by remember { mutableStateOf(false) }

    if (quantity == 0) showDialog = true

    if (showDialog) {
        androidx.compose.material.AlertDialog(
            onDismissRequest = { showDialog = false },
            backgroundColor = Color.White,
            text = {
                Text(
                    text = "Do you want to delete this product from cart?",
                    fontSize = 16.sp
                )
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    changeQuantity(1)
                }) {
                    androidx.compose.material3.Text(
                        text = "Cancel",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = androidx.compose.material.MaterialTheme.colors.primaryVariant
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    onEvent(CartEvent.OnCartProductDelete(productId))
                }) {
                    androidx.compose.material3.Text(
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