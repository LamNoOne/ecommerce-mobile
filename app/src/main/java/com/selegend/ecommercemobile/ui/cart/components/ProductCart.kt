package com.selegend.ecommercemobile.ui.cart.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextOverflow
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
    updateTotalPayment: () -> Unit,
    productCart: ProductPayment
) {


//    ConfirmDialog(
//        onEvent = onEvent,
//        productId = productCart.product.id,
//        quantity = productCart.quantity
//    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(vertical = 6.dp)
            .clickable {
                onEvent(CartEvent.OnProductClick(productCart.product.id))
            },
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
            updateTotalPayment()
        })
        AsyncImage(
            model = productCart.product.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .height(240.dp)
                .padding(12.dp)
                .aspectRatio(1f),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 6.dp, bottom = 6.dp, end = 12.dp, start = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = productCart.product.name,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.width(168.dp)
                )
                IconButton(modifier = Modifier.weight(1f), onClick = {
                    onEvent(
                        CartEvent.OnCartProductDelete(productCart.product.id)
                    )
                }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete product")
                }
            }
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
                    value = productCart.quantity.toString(),
                    onValueIncreaseClick = {
                        onEvent(
                            CartEvent.OnChangeProductQuantity(
                                productCart.product.id,
                                productCart.quantity + 1
                            )
                        )
                    },
                    onValueDecreaseClick = {
                        if (productCart.quantity > 1) {
                            onEvent(
                                CartEvent.OnChangeProductQuantity(
                                    productCart.product.id,
                                    productCart.quantity - 1
                                )
                            )
                        }
                    },
//                    onValueClearClick = {
//                        valueCounter = 0
//                    },
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