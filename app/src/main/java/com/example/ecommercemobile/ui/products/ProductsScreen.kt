package com.example.ecommercemobile.ui.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.ecommercemobile.store.domain.model.core.Product
import com.example.ecommercemobile.ui.events.ProductListEvent
import com.example.ecommercemobile.ui.home.components.ProductCard
import com.example.ecommercemobile.ui.utils.UIEvent
import kotlinx.coroutines.launch

@Composable
fun ProductsScreen(
    onNavigate: (UIEvent.Navigate) -> Unit,
    viewModel: ProductsViewModel = hiltViewModel()
) {
    val productsData: LazyPagingItems<Product> = viewModel.productPager.collectAsLazyPagingItems()

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

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(productsData.itemCount) { index ->
            ProductCard(
                modifier = Modifier
                    .width(194.dp)
                    .fillMaxHeight()
                    .clickable {
                        viewModel.onEvent(ProductListEvent.OnProductClick(productsData[index]!!.id))
                    },
                product = productsData[index]!!
            )
        }
    }
}