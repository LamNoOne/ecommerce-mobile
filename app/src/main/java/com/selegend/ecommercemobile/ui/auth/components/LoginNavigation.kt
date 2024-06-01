package com.selegend.ecommercemobile.ui.auth.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun LoginNavigation(toggleLogin: () -> Unit) {
    val uiColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    Row(
        modifier = Modifier
            .fillMaxHeight(fraction = 0.8f)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "If you have an account",
            color = Color(0xFF64748B),
            fontSize = MaterialTheme.typography.labelMedium.fontSize,
            fontWeight = FontWeight.Normal
        )
        TextButton(onClick = { toggleLogin() }) {
            Text(
                text = "Login now",
                color = uiColor,
                fontSize = MaterialTheme.typography.labelMedium.fontSize,
                fontWeight = FontWeight.Medium
            )
        }
    }
}