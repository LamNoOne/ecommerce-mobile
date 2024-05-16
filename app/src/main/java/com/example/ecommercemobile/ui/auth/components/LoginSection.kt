package com.example.ecommercemobile.ui.auth.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ecommercemobile.store.domain.model.core.LoginCredentials
import com.example.ecommercemobile.ui.auth.AuthEvent
import com.example.ecommercemobile.ui.auth.AuthViewModel
import com.example.ecommercemobile.ui.theme.BlueGray

@Composable
fun LoginSection(viewModel: AuthViewModel) {

    LoginTextField(
        label = "Username",
        trailing = "",
        modifier = Modifier.fillMaxWidth(),
        text = viewModel.username,
        onValueChange = { viewModel.onEvent(AuthEvent.OnUsernameChange(it)) }
    )
    Spacer(modifier = Modifier.height(15.dp))
    LoginTextField(
        modifier = Modifier.fillMaxWidth(),
        label = "Password",
        trailing = "Forgot?",
        text = viewModel.password,
        onValueChange = { viewModel.onEvent(AuthEvent.OnPasswordChange(it)) },
    )
    Spacer(modifier = Modifier.height(20.dp))
    Button(
        onClick = {
            viewModel.onEvent(
                AuthEvent.OnLoginClick(
                    LoginCredentials(
                        username = viewModel.username,
                        password = viewModel.password
                    )
                )
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(36.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSystemInDarkTheme()) BlueGray else Color.Black,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(size = 4.dp)
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
}