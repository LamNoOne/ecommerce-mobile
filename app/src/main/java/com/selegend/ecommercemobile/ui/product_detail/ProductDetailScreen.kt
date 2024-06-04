@file:OptIn(ExperimentalMaterial3Api::class)

package com.selegend.ecommercemobile.ui.product_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.selegend.ecommercemobile.ui.components.CounterButton
import com.selegend.ecommercemobile.ui.product_detail.components.SpecificationItem
import com.selegend.ecommercemobile.ui.utils.UIEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UIEvent.Navigate) -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
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
    val cartState by viewModel.cartState.collectAsStateWithLifecycle()
    val sheetState = androidx.compose.material3.rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable { mutableStateOf(false) }
    var valueCounter by remember {
        mutableStateOf(1)
    }

    if (state.isLoading) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                modifier = Modifier.width(56.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
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

                        IconButton(onClick = {
                            viewModel.onEvent(ProductDetailEvent.OnCartClick)
                        }) {
                            Box() {
                                cartState.cart?.let {
                                    Text(
                                        text = "${it.total}",
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
                                androidx.compose.material3.Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Shopping cart"
                                )
                            }
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
                    BottomNavigationItem(
                        icon = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.AddShoppingCart,
                                    contentDescription = null,
                                )
                                Text(text = "Add to cart", fontSize = 14.sp, fontWeight = FontWeight.Normal)
                            }
                        },
                        selected = true,
                        onClick = { isSheetOpen = true }
                    )
//                    BottomNavigationItem(
//                        icon = { Icon(Icons.Default.Payment, contentDescription = "Payment") },
//                        label = { androidx.compose.material.Text("Payment") },
//                        selected = true,
//                        onClick = { }
//                    )
                }
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = it.calculateTopPadding(),
                        bottom = it.calculateBottomPadding()
                    )
            ) {
                item {
                    AsyncImage(
                        model = state.product?.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .aspectRatio(1f)
                            .padding(8.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text =
                        ("${state.product?.name}" +
                                ", ${state.product?.processor} chip" +
                                ", ${state.product?.dimensions} inches" +
                                ", ${state.product?.ram} GB" +
                                ", ${state.product?.storageCapacity} GB SSD")
                            ?: "",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Price: ", fontSize = 16.sp, fontWeight = FontWeight.Normal)
                        Text(
                            text = "$${state.product?.price.toString()}" ?: "",
                            color = Color.Red,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                                .clip(RoundedCornerShape(8.dp))
                                .padding(top = 8.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
                        ) {
                            Text(
                                text = "Specifications",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            state.product?.screen?.let { it ->
                                SpecificationItem(
                                    title = "Screen size",
                                    value = it,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                                        .background(Color.LightGray)
                                        .padding(8.dp)
                                )
                            }
                            state.product?.operatingSystem?.let { it ->
                                SpecificationItem(
                                    title = "Operating system",
                                    value = it,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            state.product?.processor?.let { it ->
                                SpecificationItem(
                                    title = "Processor",
                                    value = it,
                                    modifier = Modifier
                                        .background(Color.LightGray)
                                        .padding(8.dp)
                                )
                            }
                            state.product?.ram?.let { it ->
                                SpecificationItem(
                                    title = "Ram",
                                    value = it.toString(),
                                    unit = "GB",
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            state.product?.storageCapacity?.let { it ->
                                SpecificationItem(
                                    title = "Storage",
                                    value = it.toString(),
                                    unit = "GB",
                                    modifier = Modifier
                                        .background(Color.LightGray)
                                        .padding(8.dp)
                                )
                            }
                            state.product?.dimensions?.let { it ->
                                SpecificationItem(
                                    title = "Dimensions",
                                    value = it,
                                    unit = "inches",
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            state.product?.weight?.let { it ->
                                SpecificationItem(
                                    title = "Weight",
                                    value = it,
                                    unit = "g",
                                    modifier = Modifier
                                        .background(Color.LightGray)
                                        .padding(8.dp)
                                )
                            }
                            state.product?.batteryCapacity?.let { it ->
                                SpecificationItem(
                                    title = "Battery",
                                    value = it.toString(),
                                    unit = "mAh",
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            state.product?.frontCameraResolution?.let { it ->
                                SpecificationItem(
                                    title = "Front camera resolution",
                                    value = it,
                                    unit = "pixels",
                                    modifier = Modifier
                                        .background(Color.LightGray)
                                        .padding(8.dp)
                                )
                            }
                            state.product?.rearCameraResolution?.let { it ->
                                SpecificationItem(
                                    title = "Rear camera resolution",
                                    value = it,
                                    unit = "pixels",
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            state.product?.connectivity?.let { it ->
                                SpecificationItem(
                                    title = "Connectivity",
                                    value = it,
                                    modifier = Modifier
                                        .clip(
                                            RoundedCornerShape(
                                                bottomStart = 8.dp,
                                                bottomEnd = 8.dp
                                            )
                                        )
                                        .background(Color.LightGray)
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
//                    AccessoryScreen(
//                        onNavigate = onNavigate,
//                        title = "Accessories purchased together"
//                    )
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
                                .height(140.dp)
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            AsyncImage(
                                model = state.product?.imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .width(140.dp)
                                    .aspectRatio(1f),
                                contentScale = ContentScale.FillBounds
                            )
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Bottom
                            ) {
                                Text(
                                    text = state.product?.name ?: "",
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Price: ",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                    Text(
                                        text = "$${state.product?.price.toString()}" ?: "",
                                        color = Color.Red,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                text = "Quantity",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal
                            )
                            CounterButton(
                                value = valueCounter.toString(),
                                onValueIncreaseClick = {
                                    valueCounter = minOf(valueCounter + 1, 99)
                                },
                                onValueDecreaseClick = {
                                    valueCounter = maxOf(valueCounter - 1, 1)
                                },
//                                onValueClearClick = {
//                                    valueCounter = 0
//                                },
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(32.dp)
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .background(Color.White)
                                .padding(16.dp)
                        ) {
                            Button(
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimaryContainer),
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(0.dp),
                                onClick = {
                                    viewModel.state.value.product?.let { it1 ->
                                        ProductDetailEvent.OnAddToCartClick(
                                            it1.id,
                                            valueCounter
                                        )
                                    }?.let { it2 ->
                                        viewModel.onEvent(
                                            it2
                                        )
                                    }
                                    runBlocking {
                                        delay(500)
                                        isSheetOpen = false
                                    }
                                }) {
                                Text(
                                    text = "Add to cart",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun Modifier.badgeLayout() =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        // based on the expectation of only one line of text
        val minPadding = placeable.height / 4

        val width = maxOf(placeable.width + minPadding, placeable.height) / 2
        layout(width, placeable.height / 2) {
            placeable.place((width - placeable.width) / 2, -placeable.height / 4)
        }
    }