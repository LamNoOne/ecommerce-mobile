package com.selegend.ecommercemobile.ui.home.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.selegend.ecommercemobile.ui.events.ProductListEvent
import com.selegend.ecommercemobile.ui.home.components.ProductCard
import com.selegend.ecommercemobile.ui.home.viewstates.ProductCategoryViewState
import com.selegend.ecommercemobile.ui.utils.ShimmerListItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

@Composable
internal fun ProductCategoryContent(
    state: StateFlow<ProductCategoryViewState>,
    onEvent: (ProductListEvent) -> Unit,
) {
    val productState by state.collectAsState()
    var isLoading by remember { mutableStateOf(productState.isLoading) }

    LaunchedEffect(key1 = true) {
        delay(300)
        isLoading = false
    }

    val title = productState.products?.name

    if (title != null) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(8.dp)
        )
    }

    val itemSize: Dp = (LocalConfiguration.current.screenWidthDp / 2 - 11).dp

    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(8.dp)
            .fillMaxSize()
    ) {
        productState.products?.let {
            items(it.products) { product ->
                ShimmerListItem(isLoading = isLoading, contentAfterLoading = {
                    ProductCard(
                        modifier = Modifier
                            .width(itemSize)
                            .fillMaxHeight()
                            .clickable {
                                onEvent(ProductListEvent.OnProductClick(product.id))
                            },
                        product = product
                    )
                })
            }
        }
    }
}