package com.example.ecommercemobile.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ecommercemobile.ui.home.components.ProductCard

@Composable
fun SmartPhoneScreen(
    modifier: Modifier = Modifier,
    viewModel: SmartPhoneViewModel = hiltViewModel()
) {
    viewModel.init()
    val state by viewModel.state.collectAsStateWithLifecycle()
    Row(modifier = Modifier.height(620.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            ProductCategoryContent(state = state)
        }
    }
}

@Composable
fun LaptopScreen(
    modifier: Modifier = Modifier,
    viewModel: LaptopViewModel = hiltViewModel()
) {
    viewModel.init()
    val state by viewModel.state.collectAsStateWithLifecycle()
    Row(modifier = Modifier.height(620.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            ProductCategoryContent(state = state)
        }
    }
}

@Composable
internal fun ProductCategoryContent(
    state: ProductCategoryViewState
) {
    state.products.forEach { (category, products) ->
        Text(
            text = category,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(8.dp)
        )
        LazyHorizontalGrid(
            rows = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .padding(8.dp)
                .fillMaxSize()
        ) {
            items(products) { product ->
                ProductCard(
                    modifier = Modifier
                        .width(194.dp)
                        .fillMaxHeight(),
                    product = product
                )
            }
        }
    }
}