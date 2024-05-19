package com.example.ecommercemobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private const val ICON_BUTTON_ALPHA_INITIAL = 0.3f
private const val CONTAINER_BACKGROUND_ALPHA_INITIAL = 0.6f

@Composable
fun ButtonContainer(
    onValueDecreaseClick: () -> Unit,
    onValueIncreaseClick: () -> Unit,
    onValueClearClick: () -> Unit,
    modifier: Modifier = Modifier,
    clearButtonVisible: Boolean = false,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Black.copy(alpha = CONTAINER_BACKGROUND_ALPHA_INITIAL))
            .padding(horizontal = 8.dp)
    ) {
        // decrease button
        IconControlButton(
            icon = Icons.Outlined.Remove,
            contentDescription = "Decrease count",
            onClick = onValueDecreaseClick,
            tintColor = Color.White.copy(alpha = ICON_BUTTON_ALPHA_INITIAL)
        )

        // clear button
        if (clearButtonVisible) {
            IconControlButton(
                icon = Icons.Outlined.Clear,
                contentDescription = "Clear count",
                onClick = onValueClearClick,
                tintColor = Color.White.copy(alpha = ICON_BUTTON_ALPHA_INITIAL)
            )
        }

        // increase button
        IconControlButton(
            icon = Icons.Outlined.Add,
            contentDescription = "Increase count",
            onClick = onValueIncreaseClick,
            tintColor = Color.White.copy(alpha = ICON_BUTTON_ALPHA_INITIAL)
        )
    }
}