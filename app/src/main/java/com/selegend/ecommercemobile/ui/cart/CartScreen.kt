package com.selegend.ecommercemobile.ui.cart

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.selegend.ecommercemobile.store.domain.model.core.carts.ProductCart
import com.selegend.ecommercemobile.store.domain.model.core.products.Product
import com.selegend.ecommercemobile.ui.cart.components.ProductCart
import com.selegend.ecommercemobile.ui.product_detail.badgeLayout
import com.selegend.ecommercemobile.ui.utils.UIEvent

@Composable
fun CartScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UIEvent.Navigate) -> Unit,
    viewModel: CartViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scaffoldState = rememberScaffoldState()
    var checked by remember { mutableStateOf(false) }
    var isAtLeastOneChecked by remember { mutableStateOf(false) }
    var selectedItem = remember {
        mutableListOf<ProductCart>()
    }

    val productsData: LazyPagingItems<Product> =
        viewModel.productPager.collectAsLazyPagingItems()

    Log.d("CartScreen", "CartScreen product state: ${productsData.itemCount}")


    fun updateSelectedItem(item: ProductCart) {
        selectedItem.add(item)
    }

    fun deleteSelectedItem(item: ProductCart) {
        selectedItem.remove(item)
    }

    fun updateAllChecked(status: Boolean) {
        checked = status
    }

    fun updateAtLeastOneChecked(status: Boolean) {
        isAtLeastOneChecked = status
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                }
                is UIEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }
    if (state.isLoading) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(2f)
                .background(Color.Transparent),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.width(56.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
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
                    horizontalArrangement = Arrangement.SpaceBetween
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
                            text = "Shopping cart",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        state.cart?.total?.let {
                            Text(
                                text = "($it)",
                                fontSize = 12.sp,
                                color = Color.DarkGray
                            )
                        }
                    }

                    IconButton(onClick = { /*TODO*/ }) {
                        Box() {
                            state.cart?.total?.let {
                                Text(
                                    text = it.toString(),
                                    color = Color.White,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier
                                        .background(
                                            MaterialTheme.colorScheme.error,
                                            shape = CircleShape
                                        )
                                        .badgeLayout()
                                        .padding(end = 0.dp)
                                        .zIndex(2f)
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Shopping cart"
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (!state.isLoading) {
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
                            Checkbox(
                                checked = checked,
                                onCheckedChange = {
                                    checked = it
                                    if (it) {
                                        selectedItem.clear()
                                        state.cart?.products?.let { it1 ->
                                            for (i in it1) {
                                                selectedItem.add(i)
                                            }
                                        }
                                    } else {
                                        selectedItem.clear()
                                    }
                                })
                            Text(
                                text = "All items",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.DarkGray,
                            )
                        }
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "Total payment: ",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Black
                                )
                                Text(
                                    text = "$${totalPayment(state)}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Red
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    Log.d("CartScreen", "CartScreen selected items: $selectedItem")
                                    Log.d(
                                        "CartScreen",
                                        "CartScreen selected quantity: ${selectedItem.size}"
                                    )
                                    if (selectedItem.size > 0) {
                                        viewModel.onEvent(CartEvent.OnCheckoutClick(selectedItem))
                                    } else {
                                        Log.d(
                                            "CartScreen",
                                            "CartScreen: Please select at least one item"
                                        )
                                        viewModel.onEvent(CartEvent.OnShowSnackBar("Please select at least one item"))
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(0.dp),
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(120.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Checkout",
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
        }
    ) { it ->
        LazyVerticalStaggeredGrid(
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding(),
                start = 8.dp,
                end = 8.dp
            ),
            columns = StaggeredGridCells.Fixed(1),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalItemSpacing = 10.dp
        ) {
            Log.d("CartScreen", "CartScreen: $state")
            state.cart?.products?.let { it1 ->
                items(it1.count()) { index ->
                    var itemChecked by remember {
                        mutableStateOf(false)
                    }

                    fun updateItemChecked(status: Boolean) {
                        itemChecked = status
                    }

                    if (selectedItem.size == state.cart?.products?.size) {
                        checked = true
                    }

                    if (checked) {
                        itemChecked = true
                    }

                    if (!checked && selectedItem.size == 0) {
                        itemChecked = false
                    }

                    ProductCart(
                        onPopBackStack = onPopBackStack,
                        onEvent = viewModel::onEvent,
                        updateAllChecked = { updateAllChecked(it) },
                        updateItemChecked = { updateItemChecked(it) },
                        itemChecked = itemChecked,
                        updateSelectedItem = { updateSelectedItem(it) },
                        deleteSelectedItem = { deleteSelectedItem(it) },
                        productCart = it1[index],
                        modifier = Modifier
                            .fillMaxSize()
                            .height(100.dp)
                    )
                }
            }
        }
    }
}


private fun totalPayment(state: CartViewState): Double {
    var total = 0.0
    state.cart?.products?.let {
        for (i in it) {
            total += i.product.price * i.quantity
        }
    }
    return total
}