package com.selegend.ecommercemobile.ui.profile.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun EditTextField(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
) {
    val uiColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    var isFocused by remember { mutableStateOf(false) }
    Column(modifier = modifier.fillMaxWidth()) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = uiColor)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = text,
            onValueChange = {
                onValueChange(it)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = uiColor.copy(alpha = 0.5f)
                )
            },
            colors = TextFieldDefaults.colors(
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary,
                focusedPlaceholderColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
            ),
            singleLine = true,
            enabled = isFocused,
            trailingIcon = {
                // Enable when focus, disable when unfocus
                IconButton(
                    onClick = {
                        isFocused = !isFocused
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit"
                    )
                }
            }
        )
    }
}