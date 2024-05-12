package com.example.ecommercemobile.ui.auth.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ecommercemobile.R

@Composable
fun SocialMediaSection() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Or continue with",
            style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFF64748B))
        )
        Spacer(modifier = androidx.compose.ui.Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SocialMediaLogin(
                modifier = Modifier.weight(1f),
                icon = R.drawable.google,
                text = "Google",
                onClick = {}
            )
            Spacer(modifier = Modifier.width(20.dp))
            SocialMediaLogin(
                modifier = Modifier.weight(1f),
                icon = R.drawable.facebook,
                text = "Facebook",
                onClick = {}
            )
        }
    }
}