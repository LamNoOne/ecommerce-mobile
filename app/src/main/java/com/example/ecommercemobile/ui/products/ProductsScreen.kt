package com.example.ecommercemobile.ui.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.ecommercemobile.store.domain.model.core.Product
import com.example.ecommercemobile.ui.home.components.ProductCard

@Composable
fun ProductsScreen(viewModel: ProductsViewModel = hiltViewModel()) {
    val productsData: LazyPagingItems<Product> = viewModel.productPager.collectAsLazyPagingItems()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(productsData.itemCount) {index ->
            ProductCard(
                modifier = Modifier
                    .width(194.dp)
                    .fillMaxHeight()
                    .clickable {
                        /* TODO */
                    },
                product = productsData[index]!!
            )
        }
    }
}