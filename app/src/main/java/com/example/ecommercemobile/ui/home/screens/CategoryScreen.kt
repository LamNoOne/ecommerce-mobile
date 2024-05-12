package com.example.ecommercemobile.ui.home.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ecommercemobile.ui.home.components.CategoryItem
import com.example.ecommercemobile.ui.home.viewmodels.CategoryViewModel
import com.example.ecommercemobile.ui.utils.UIEvent

@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
    onNavigate: (UIEvent.Navigate) -> Unit,
    viewModel: CategoryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }

    Row(modifier = Modifier.height(200.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(8.dp)
            )
            LazyHorizontalGrid(
                rows = GridCells.Fixed(1), verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    .padding(8.dp)
                    .fillMaxSize()
            ) {
                items(state.categories) { category ->
                    CategoryItem(
                        modifier = Modifier
                            .height(80.dp)
                            .width(100.dp),
                        category = category,
                        onEvent = viewModel::onEvent
                    )
                }
            }
        }
    }
}