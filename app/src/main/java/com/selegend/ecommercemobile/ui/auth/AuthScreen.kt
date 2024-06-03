package com.selegend.ecommercemobile.ui.auth

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.selegend.ecommercemobile.store.domain.model.core.auth.OauthCredentials
import com.selegend.ecommercemobile.ui.auth.components.*
import com.selegend.ecommercemobile.ui.utils.UIEvent
import com.selegend.ecommercemobile.utils.Constants
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import com.stevdzasan.onetap.getUserFromTokenId
import com.stevdzasan.onetap.rememberOneTapSignInState
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UIEvent.Navigate) -> Unit,
    viewModel: AuthViewModel = hiltViewModel() // sd thu vien
) {

    val oneTapSignInState = rememberOneTapSignInState()
    val authenticated = remember {
        mutableStateOf(false)
    }

    OneTapSignInWithGoogle(
        state = oneTapSignInState,
        clientId = Constants.GOOGLE_CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            authenticated.value = true
            val authResponse = getUserFromTokenId(tokenId)
            if (authResponse != null) {
                val (sub, email, emailVerified, fullName, givenName, familyName, picture, issuedAt, expirationTime, locale) = authResponse
                val authData = OauthCredentials(
                    oauthId = sub!!,
                    email = email!!,
                    firstName = givenName!!,
                    lastName = familyName!!,
                    imageUrl = picture!!,
                    username = email
                )
                viewModel.onEvent(AuthEvent.OnOauthClick(authData))
            }
        },
        onDialogDismissed = { message ->
            Log.d("OneTapSignIn", "Dialog dismissed: $message")
        }
    )

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
            SocialMediaSection(oneTapSignInState = oneTapSignInState)
            Spacer(modifier = Modifier.height(25.dp))
            if (isLogin) {
                SignupNavigation(toggleLogin = { toggleLogin() })
            } else {
                LoginNavigation(toggleLogin = { toggleLogin() })
            }
        }
    }
}