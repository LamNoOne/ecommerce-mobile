package com.example.ecommercemobile.ui.auth.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun SignupNavigation() {
    val uiColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    Box(
        modifier = Modifier
            .fillMaxHeight(fraction = 0.8f)
            .fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Text(text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    color = Color(0xFF64748B),
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    fontWeight = FontWeight.Normal
                )
            ) {
                append("Don't have an account? ")
            }

            withStyle(
                style = SpanStyle(
                    color = uiColor,
                    fontSize = MaterialTheme.typography.labelMedium.fontSize,
                    fontWeight = FontWeight.Medium
                )
            ) {
                append("Create now")
            }
        })
    }
}