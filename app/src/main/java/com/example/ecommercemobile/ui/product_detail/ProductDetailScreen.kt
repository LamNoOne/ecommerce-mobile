package com.example.ecommercemobile.ui.product_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.ecommercemobile.ui.home.screens.AccessoryScreen
import com.example.ecommercemobile.ui.product_detail.components.SpecificationItem
import com.example.ecommercemobile.ui.utils.UIEvent
import kotlinx.coroutines.launch

@Composable
fun ProductDetailScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UIEvent.Navigate) -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.ShowSnackBar -> {
                    scope.launch {
                        scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                        val result = scaffoldState.snackbarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = event.action,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
                is UIEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }

    if (state.isLoading) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                Icons.Default.AddShoppingCart,
                                contentDescription = "Add to cart"
                            )
                        },
                        label = { androidx.compose.material.Text("Add to cart") },
                        selected = true,
                        onClick = { }
                    )
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.Payment, contentDescription = "Payment") },
                        label = { androidx.compose.material.Text("Payment") },
                        selected = true,
                        onClick = { }
                    )
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
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
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
                    Row(modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)) {
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
                    AccessoryScreen(onNavigate = onNavigate, title = "Accessories purchased together")
                }
            }
        }
    }
}