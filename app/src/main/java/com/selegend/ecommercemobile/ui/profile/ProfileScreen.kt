package com.selegend.ecommercemobile.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.selegend.ecommercemobile.ui.auth.AuthViewModel
import com.selegend.ecommercemobile.ui.cart.CartViewModel
import com.selegend.ecommercemobile.ui.home.events.CoreEvent
import com.selegend.ecommercemobile.ui.home.viewmodels.CoreViewModel
import com.selegend.ecommercemobile.ui.product_detail.badgeLayout
import com.selegend.ecommercemobile.ui.utils.UIEvent
import kotlinx.coroutines.flow.merge

@Composable
fun ProfileScreen(
    onPopBackStack: () -> Unit,
    onNavigate: (UIEvent.Navigate) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
    cartViewModel: CartViewModel = hiltViewModel(),
    coreViewModel: CoreViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = true) {
        merge(viewModel.uiEvent, coreViewModel.uiEvent).collect { event ->
            when (event) {
                is UIEvent.PopBackStack -> onPopBackStack()
                is UIEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action,
                        duration = SnackbarDuration.Short
                    )
                }
                is UIEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }

    val cartState by cartViewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        bottomBar = {
            androidx.compose.material3.BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home Icon") },
                    label = { Text("Home") },
                    selected = false,
                    onClick = { coreViewModel.onEvent(CoreEvent.OnHomeClick) }
                )
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorite Icon") },
                    label = { Text("Favorite") },
                    selected = false,
                    onClick = { coreViewModel.onEvent(CoreEvent.OnFavoriteClick) }
                )
                BottomNavigationItem(
                    icon = {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = "Shopping Cart Icon"
                        )
                    },
                    label = { Text("Cart") },
                    selected = false,
                    onClick = { coreViewModel.onEvent(CoreEvent.OnCartClick) }
                )
                if (authViewModel.auth != null) {
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = "Person Icon") },
                        label = { Text(authViewModel.auth!!.firstName) },
                        selected = true,
                        onClick = { coreViewModel.onEvent(CoreEvent.OnProfileClick) }
                    )
                } else {
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = "Person Icon") },
                        label = { Text("Log in") },
                        selected = false,
                        onClick = { coreViewModel.onEvent(CoreEvent.OnLoginClick) }
                    )
                }
            }
        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = "https://res.cloudinary.com/dmnzkqysq/image/upload/v1716885512/z5484451765803_3e4d26b21b3ae2af6ff0234020265832_rzgv7v.jpg",
                            contentDescription = null,
                            modifier = Modifier
                                .height(60.dp)
                                .padding(6.dp)
                                .aspectRatio(1f),
                            contentScale = ContentScale.FillBounds
                        )
                        Column(modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)) {
                            Text(
                                text = "Tran Nguyen Dac Lam",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    IconButton(onClick = { /*TODO*/ }) {
                        Box() {
                            cartState.cart?.total?.let {
                                Text(
                                    text = it.toString(),
                                    color = Color.White,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier
                                        .background(
                                            MaterialTheme.colorScheme.error,
                                            shape = CircleShape
                                        )
                                        .badgeLayout()
                                        .padding(end = 0.dp)
                                        .zIndex(2f)
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Shopping cart"
                            )
                        }
                    }
                }
            }
        }
    }
}