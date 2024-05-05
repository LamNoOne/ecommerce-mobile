package com.example.ecommercemobile.ui.home.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ecommercemobile.ui.home.viewmodels.CameraViewModel
import com.example.ecommercemobile.ui.utils.UIEvent

@Composable
fun CameraScreen(
    modifier: Modifier = Modifier,
    onNavigate: (UIEvent.Navigate) -> Unit,
    viewModel: CameraViewModel = hiltViewModel()
) {
    viewModel.init()
    val state by viewModel.state.collectAsStateWithLifecycle()
    Row(modifier = Modifier.height(620.dp)) {
        Column(modifier = Modifier.weight(1f)) {
            ProductCategoryContent(state = state, onEvent = viewModel::onEvent)
        }
    }
}