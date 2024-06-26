package com.selegend.ecommercemobile.ui.auth.components

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
import com.selegend.ecommercemobile.store.domain.model.core.auth.LoginCredentials
import com.selegend.ecommercemobile.ui.auth.AuthEvent
import com.selegend.ecommercemobile.ui.auth.AuthViewModel
import com.selegend.ecommercemobile.ui.theme.BlueGray

@Composable
fun LoginSection(viewModel: AuthViewModel) {
    AuthTextField(
        label = "Username",
        modifier = Modifier.fillMaxWidth(),
        text = viewModel.username,
        errorMessage = "Username is required",
        errorHandler = { it.isEmpty() },
        onValueChange = { viewModel.onEvent(AuthEvent.OnUsernameChange(it)) }
    )
    Spacer(modifier = Modifier.height(15.dp))
    PasswordTextField(
        modifier = Modifier.fillMaxWidth(),
        label = "Password",
        text = viewModel.password,
        errorMessage = "Password is required",
        errorHandler = {
            it.isEmpty() || it.length < 6
        },
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
            .height(40.dp),
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