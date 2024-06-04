package com.selegend.ecommercemobile.ui.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import com.selegend.ecommercemobile.R
import com.selegend.ecommercemobile.store.domain.model.core.payment.ProductPayment
import com.selegend.ecommercemobile.store.domain.model.core.products.Product
import com.selegend.ecommercemobile.ui.cart.components.ProductCart
import com.selegend.ecommercemobile.ui.home.components.ProductCard
import com.selegend.ecommercemobile.ui.product_detail.badgeLayout
import com.selegend.ecommercemobile.ui.utils.UIEvent

@Composable
fun CartScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UIEvent.Navigate) -> Unit,
    viewModel: CartViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.PopBackStack -> onPopBackStack()
                is UIEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action,
                        duration = SnackbarDuration.Short
                    )
                }
                is UIEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val productsData: LazyPagingItems<Product> = viewModel.productPager.collectAsLazyPagingItems()
    var checked by remember { mutableStateOf(false) }
    var totalPayment by remember { mutableStateOf(0.0) }

    val selectedItem = remember {
        mutableListOf<ProductPayment>()
    }

    fun updateTotalPayment() {
        totalPayment = selectedItem.sumOf { it.product.price * it.quantity }.toDouble()
    }

    fun updateSelectedItem(item: ProductPayment) {
        selectedItem.add(item)
    }

    fun deleteSelectedItem(item: ProductPayment) {
        selectedItem.remove(item)
    }

    fun updateAllChecked(status: Boolean) {
        checked = status
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
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
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
                                    updateTotalPayment()
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
                                    text = "$$totalPayment",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Red
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (selectedItem.size > 0) {
                                        viewModel.onEvent(CartEvent.OnCheckoutClick(selectedItem))
                                    } else {
                                        viewModel.onEvent(CartEvent.OnShowSnackBar("Please select at least one item"))
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSecondaryContainer),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(120.dp)
                                    .padding(top = 4.dp, end = 4.dp),
                                enabled = selectedItem.size > 0 && viewModel.authState != null
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
        LazyColumn(
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding(),
                start = 8.dp,
                end = 8.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
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
                        updateTotalPayment = { updateTotalPayment() },
                        productCart = it1[index],
                        modifier = Modifier
                            .fillMaxSize()
                            .height(100.dp)
                    )
                }
            }
            item {
                if (state.cart?.products?.size == 0 || viewModel.authState == null) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Your cart is empty",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                        Image(
                            painter = painterResource(id = R.drawable.cart),
                            contentDescription = "Cart is empty",
                            modifier = Modifier.size(64.dp)
                        )
                        Button(
                            onClick = {
                                viewModel.onEvent(CartEvent.OnBackToHome)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSecondaryContainer),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp, end = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Continue shopping",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
            item {
                val dividerSize: Dp = (LocalConfiguration.current.screenWidthDp / 2 - 90).dp
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .background(Color.Transparent),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.LightGray,
                        modifier = Modifier.width(dividerSize)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Recommended for you", fontSize = 14.sp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.width(4.dp))
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color.LightGray,
                        modifier = Modifier.width(dividerSize)
                    )
                }
            }
            items(productsData.itemCount) { index ->
                com.google.accompanist.flowlayout.FlowRow(
                    mainAxisSize = SizeMode.Expand,
                    mainAxisAlignment = FlowMainAxisAlignment.SpaceBetween
                ) {
                    val itemSize: Dp = (LocalConfiguration.current.screenWidthDp / 2 - 11).dp
                    ProductCard(
                        modifier = Modifier
                            .width(itemSize)
                            .height(268.dp)
                            .clickable {
                                viewModel.onEvent(CartEvent.OnProductClick(productsData[index * 2]!!.id))
                            },
                        product = productsData[index * 2]!!,
                    )
                    if (index * 2 + 1 < productsData.itemCount) {
                        ProductCard(
                            modifier = Modifier
                                .width(itemSize)
                                .height(268.dp)
                                .clickable {
                                    viewModel.onEvent(CartEvent.OnProductClick(productsData[index * 2 + 1]!!.id))
                                },
                            product = productsData[index * 2 + 1]!!,
                        )
                    }
                }
            }
        }
    }
}