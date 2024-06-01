package com.selegend.ecommercemobile.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun CounterButton(
    value: String,
    onValueDecreaseClick: () -> Unit,
    onValueIncreaseClick: () -> Unit,
//    onValueClearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {

        ButtonContainer(
            onValueDecreaseClick = onValueDecreaseClick,
            onValueIncreaseClick = onValueIncreaseClick,
//            onValueClearClick = onValueClearClick,
            modifier = Modifier
        )

        DraggableThumbButton(
            value = value,
            onClick = onValueIncreaseClick,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}