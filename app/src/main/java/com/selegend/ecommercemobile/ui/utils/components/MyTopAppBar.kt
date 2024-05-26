package com.selegend.ecommercemobile.ui.utils.components

import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

@Composable
fun MyTopAppBar(title: String) {
    TopAppBar(title = { Text(text = title)}, modifier = Modifier.shadow(elevation = 5.dp))
}