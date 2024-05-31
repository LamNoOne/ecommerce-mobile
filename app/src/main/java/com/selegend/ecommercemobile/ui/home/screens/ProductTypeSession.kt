package com.selegend.ecommercemobile.ui.home.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.selegend.ecommercemobile.ui.events.ProductListEvent
import com.selegend.ecommercemobile.ui.home.viewstates.ProductCategoryViewState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ProductTypeSession(
    onEvent: (ProductListEvent) -> Unit,
    state: StateFlow<ProductCategoryViewState>
) {
    Row(modifier = Modifier.height(620.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            ProductCategoryContent(state = state, onEvent = onEvent)
        }
    }
}