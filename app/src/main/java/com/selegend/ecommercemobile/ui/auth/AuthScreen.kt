package com.selegend.ecommercemobile.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.selegend.ecommercemobile.ui.auth.components.*
import com.selegend.ecommercemobile.ui.utils.UIEvent
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UIEvent.Navigate) -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    var isLogin by remember { mutableStateOf(true) }

    fun toggleLogin() {
        isLogin = !isLogin
    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.ShowSnackBar -> {
                    scope.launch {
                        scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = event.message,
                            actionLabel = event.action,
                            duration = SnackbarDuration.Short
                        )
                    }
                }
                is UIEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TopSection(isLogin = isLogin)

        Spacer(modifier = Modifier.height(26.dp))

        Column(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxSize()
        ) {
            if (isLogin) {
                LoginSection(viewModel = viewModel)
            } else {
                SignupSection(viewModel = viewModel)
            }
            Spacer(modifier = Modifier.height(25.dp))
            SocialMediaSection()
            Spacer(modifier = Modifier.height(25.dp))
            if (isLogin) {
                SignupNavigation(toggleLogin = { toggleLogin() })
            } else {
                LoginNavigation(toggleLogin = { toggleLogin() })
            }
        }
    }
}