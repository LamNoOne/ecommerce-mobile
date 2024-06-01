package com.selegend.ecommercemobile.ui.auth.components

import android.util.Patterns
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.selegend.ecommercemobile.ui.auth.AuthEvent
import com.selegend.ecommercemobile.ui.auth.AuthViewModel
import com.selegend.ecommercemobile.ui.theme.BlueGray

@Composable
fun SignupSection(viewModel: AuthViewModel) {

    val isSignUpEnabled = remember {
        derivedStateOf {
            viewModel.firstNameSignup.isNotEmpty() &&
                    viewModel.lastNameSignup.isNotEmpty() &&
                    Patterns.EMAIL_ADDRESS.matcher(viewModel.emailSignup).matches() &&
                    Patterns.PHONE.matcher(viewModel.phoneNumber).matches() &&
                    viewModel.usernameSignup.isNotEmpty() &&
                    viewModel.usernameSignup.length >= 6 &&
                    viewModel.passwordSignup.isNotEmpty() &&
                    viewModel.passwordSignup.length >= 6 &&
                    viewModel.passwordSignup == viewModel.confirmPasswordSignup
        }
    }

    AuthTextField(
        label = "FirstName",
        modifier = Modifier.fillMaxWidth(),
        text = viewModel.firstNameSignup,
        errorMessage = "FirstName is required",
        errorHandler = { it.isEmpty() },
        onValueChange = { viewModel.onEvent(AuthEvent.OnFirstNameChange(it)) }
    )
    Spacer(modifier = Modifier.height(15.dp))
    AuthTextField(
        label = "LastName",
        modifier = Modifier.fillMaxWidth(),
        text = viewModel.lastNameSignup,
        errorMessage = "LastName is required",
        errorHandler = { it.isEmpty() },
        onValueChange = { viewModel.onEvent(AuthEvent.OnLastNameChange(it)) }
    )
    Spacer(modifier = Modifier.height(15.dp))
    AuthTextField(
        label = "Email",
        modifier = Modifier.fillMaxWidth(),
        text = viewModel.emailSignup,
        errorMessage = "Please enter a valid email",
        errorHandler = { Patterns.EMAIL_ADDRESS.matcher(it).matches().not() },
        onValueChange = { viewModel.onEvent(AuthEvent.OnEmailChange(it)) }
    )
    Spacer(modifier = Modifier.height(15.dp))
    AuthTextField(
        label = "Phone Number",
        modifier = Modifier.fillMaxWidth(),
        text = viewModel.phoneNumber,
        errorMessage = "Please enter a valid phone number",
        errorHandler = { Patterns.PHONE.matcher(it).matches().not() },
        onValueChange = { viewModel.onEvent(AuthEvent.OnPhoneNumberChange(it)) }
    )
    Spacer(modifier = Modifier.height(15.dp))
    AuthTextField(
        label = "Username",
        modifier = Modifier.fillMaxWidth(),
        text = viewModel.usernameSignup,
        errorMessage = "Please enter a valid email",
        errorHandler = { it.isEmpty() || it.length < 6 },
        onValueChange = { viewModel.onEvent(AuthEvent.OnUserNameSignupChange(it)) }
    )
    Spacer(modifier = Modifier.height(15.dp))
    PasswordTextField(
        modifier = Modifier.fillMaxWidth(),
        label = "Password",
        text = viewModel.passwordSignup,
        errorMessage = "Password contains at least 6 characters",
        errorHandler = {
            it.isEmpty() || it.length < 6
        },
        onValueChange = { viewModel.onEvent(AuthEvent.OnPasswordSignupChange(it)) },
    )
    Spacer(modifier = Modifier.height(15.dp))
    PasswordTextField(
        modifier = Modifier.fillMaxWidth(),
        label = "Confirm Password",
        text = viewModel.confirmPasswordSignup,
        errorMessage = "Password must match",
        errorHandler = {
            it != viewModel.passwordSignup
        },
        onValueChange = { viewModel.onEvent(AuthEvent.OnConfirmPasswordChange(it)) },
    )
    Spacer(modifier = Modifier.height(20.dp))
    Button(
        onClick = {
            if (isSignUpEnabled.value) {
                viewModel.onEvent(
                    AuthEvent.OnSignUpClick
                )
            }
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
            text = "Sign Up",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
}