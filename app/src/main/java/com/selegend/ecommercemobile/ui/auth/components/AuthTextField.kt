package com.selegend.ecommercemobile.ui.auth.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.selegend.ecommercemobile.R
import com.selegend.ecommercemobile.helpers.Keyboard
import com.selegend.ecommercemobile.helpers.keyboardAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthTextField(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    errorMessage: String,
    errorHandler: (String) -> Boolean,
    onValueChange: (String) -> Unit,
) {
    val isKeyboardOpen by keyboardAsState()
    val uiColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    var isError by remember {
        mutableStateOf(false)
    }

    TextField(
        value = text,
        onValueChange = {
            onValueChange(it)
            // set is error to false when user starts typing
            isError = errorHandler(it)
        },
        modifier = modifier,
        label = {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = uiColor)
        },
        colors = TextFieldDefaults.colors(
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary,
            focusedPlaceholderColor = MaterialTheme.colorScheme.primary,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
        ),
        supportingText = {
            if (isError && isKeyboardOpen == Keyboard.Closed) {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        isError = isError && isKeyboardOpen == Keyboard.Closed,
        singleLine = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    errorMessage: String,
    errorHandler: (String) -> Boolean,
    onValueChange: (String) -> Unit,
) {
    val isKeyboardOpen by keyboardAsState()
    val uiColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    var passwordVisibility: Boolean by remember { mutableStateOf(false) }

    var isError by remember {
        mutableStateOf(false)
    }

    TextField(
        value = text,
        onValueChange = {
            onValueChange(it)
            isError = errorHandler(it)
        },
        modifier = modifier,
        label = {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = uiColor)
        },
        colors = TextFieldDefaults.colors(
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary,
            focusedPlaceholderColor = MaterialTheme.colorScheme.primary,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
        ),
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = {
                passwordVisibility = !passwordVisibility
            }) {
                if (passwordVisibility)
                    Icon(
                        painter = painterResource(id = R.drawable.show_password),
                        contentDescription = "Show Password",
                        modifier = Modifier.size(24.dp)
                    )
                else
                    Icon(
                        painter = painterResource(id = R.drawable.hide_password),
                        contentDescription = "Hide Password",
                        modifier = Modifier.size(24.dp)
                    )
            }
        },
        supportingText = {
            if (isError && isKeyboardOpen == Keyboard.Closed) {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        isError = isError && isKeyboardOpen == Keyboard.Closed,
        singleLine = true
    )
}