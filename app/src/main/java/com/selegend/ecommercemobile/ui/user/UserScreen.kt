package com.selegend.ecommercemobile.ui.user

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.selegend.ecommercemobile.R
import com.selegend.ecommercemobile.ui.auth.AuthViewModel
import com.selegend.ecommercemobile.ui.components.LoadingItem
import com.selegend.ecommercemobile.ui.home.events.CoreEvent
import com.selegend.ecommercemobile.ui.home.viewmodels.CoreViewModel
import com.selegend.ecommercemobile.ui.user.components.SettingNavigation
import com.selegend.ecommercemobile.ui.utils.UIEvent
import kotlinx.coroutines.flow.merge

@Composable
fun UserScreen(
    sharedState: UserViewState,
    onPopBackStack: () -> Unit,
    onNavigate: (UIEvent.Navigate) -> Unit,
    viewModel: UserViewModel = hiltViewModel(),
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
                    )
                }
                is UIEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }

    if (sharedState.isLoading) {
        LoadingItem()
    } else {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            modifier = Modifier
                .fillMaxSize(),
            bottomBar = {
                androidx.compose.material3.BottomAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(10.dp),
                    containerColor = Color.White
                ) {
                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home Icon") },
                        label = { Text("Home") },
                        selected = false,
                        onClick = { coreViewModel.onEvent(CoreEvent.OnHomeClick) }
                    )
//                BottomNavigationItem(
//                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorite Icon") },
//                    label = { Text("Favorite") },
//                    selected = false,
//                    onClick = { coreViewModel.onEvent(CoreEvent.OnFavoriteClick) }
//                )
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
                            icon = {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "Person Icon"
                                )
                            },
                            label = { Text("Me") },
                            selected = true,
                            onClick = { coreViewModel.onEvent(CoreEvent.OnProfileClick) }
                        )
                    } else {
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "Person Icon"
                                )
                            },
                            label = { Text("Log in") },
                            selected = false,
                            onClick = { coreViewModel.onEvent(CoreEvent.OnLoginClick) }
                        )
                    }
                }
            }
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = it.calculateBottomPadding())
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AsyncImage(
                            model = "${sharedState.user?.image}",
                            contentDescription = null,
                            modifier = Modifier
                                .height(80.dp)
                                .padding(6.dp)
                                .clip(RoundedCornerShape(50))
                                .aspectRatio(1f)
                                .border(
                                    1.dp,
                                    Color.Gray.copy(alpha = 0.5f),
                                    RoundedCornerShape(50)
                                ),
                            contentScale = ContentScale.FillBounds
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${sharedState.user?.firstName} ${sharedState.user?.lastName}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${sharedState.user?.email}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SettingNavigation(
                            onClick = { viewModel.onEvent(UserEvent.OnEditProfileClick) },
                            text = "Edit Profile",
                            imageVector = Icons.Default.Person,
                            imageResource = R.drawable.arrow_right,
                        )
                        HorizontalDivider(
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.surfaceContainer
                        )
                        SettingNavigation(
                            onClick = { viewModel.onEvent(UserEvent.OnCartClick) },
                            text = "My Cart",
                            imageVector = Icons.Default.ShoppingCart,
                            imageResource = R.drawable.arrow_right,
                        )
                        HorizontalDivider(
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.surfaceContainer
                        )
//                    SettingNavigation(
//                        onClick = { /*TODO*/ },
//                        text = "My Favorite",
//                        imageVector = Icons.Default.Favorite,
//                        imageResource = R.drawable.arrow_right,
//                    )
//                    HorizontalDivider(
//                        thickness = 2.dp,
//                        color = MaterialTheme.colorScheme.surfaceContainer
//                    )
                        SettingNavigation(
                            onClick = { viewModel.onEvent(UserEvent.OnOrderHistoryClick) },
                            text = "Order History",
                            imageVector = Icons.Default.Payments,
                            imageResource = R.drawable.arrow_right,
                        )
                        HorizontalDivider(
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.surfaceContainer
                        )
                        SettingNavigation(
                            onClick = { /*TODO*/ },
                            text = "Settings",
                            imageVector = Icons.Default.Settings,
                            imageResource = R.drawable.arrow_right,
                        )
                        Button(
                            onClick = {
                                viewModel.onEvent(UserEvent.OnLogoutClick)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSecondaryContainer),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                androidx.compose.material3.Text(
                                    text = "Log out",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }
                        }
                    }
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(bottom = 16.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp),
//                            horizontalArrangement = Arrangement.Start
//                        ) {
//                            Text(
//                                text = "Last Activities",
//                                fontSize = 20.sp,
//                                fontWeight = FontWeight.Bold,
//                                letterSpacing = 1.sp
//                            )
//                        }
//                        Card(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(100.dp)
//                                .padding(horizontal = 8.dp),
//                            shape = RoundedCornerShape(10.dp),
//                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
//                            colors = CardDefaults.cardColors(containerColor = Color.White)
//                        ) {
//                            Row(
//                                modifier = Modifier.fillMaxSize(),
//                                verticalAlignment = Alignment.CenterVertically,
//                            ) {
//                                AsyncImage(
//                                    model = "https://media.licdn.com/dms/image/D5612AQFxDB38HiHngQ/article-cover_image-shrink_600_2000/0/1703142367412?e=2147483647&v=beta&t=KTZakpTszD-neJALKJqG0lvWq6DRmD6wHZ9g_Djhuqg",
//                                    contentDescription = null,
//                                    modifier = Modifier
//                                        .size(100.dp)
//                                        .padding(horizontal = 16.dp)
//                                        .aspectRatio(1f),
//                                    contentScale = ContentScale.FillBounds
//                                )
//                                Column(
//                                    modifier = Modifier
//                                        .fillMaxSize()
//                                        .padding(vertical = 16.dp),
//                                    verticalArrangement = Arrangement.SpaceBetween
//                                ) {
//                                    NotTransactionTextActivities(
//                                        text1 = "You added ",
//                                        text2 = "Simple work space",
//                                        text3 = " to Your Favorite"
//                                    )
//                                    Text(
//                                        text = "2 hours ago",
//                                        fontSize = 14.sp,
//                                        color = Color.Gray
//                                    )
//                                }
//                            }
//                        }
//
//                        Card(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(100.dp)
//                                .padding(horizontal = 8.dp),
//                            shape = RoundedCornerShape(10.dp),
//                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
//                            colors = CardDefaults.cardColors(containerColor = Color.White)
//                        ) {
//                            Row(
//                                modifier = Modifier.fillMaxSize(),
//                                verticalAlignment = Alignment.CenterVertically,
//                            ) {
//                                AsyncImage(
//                                    model = "https://media.licdn.com/dms/image/D5612AQFxDB38HiHngQ/article-cover_image-shrink_600_2000/0/1703142367412?e=2147483647&v=beta&t=KTZakpTszD-neJALKJqG0lvWq6DRmD6wHZ9g_Djhuqg",
//                                    contentDescription = null,
//                                    modifier = Modifier
//                                        .size(100.dp)
//                                        .padding(horizontal = 16.dp)
//                                        .aspectRatio(1f),
//                                    contentScale = ContentScale.FillBounds
//                                )
//                                Column(
//                                    modifier = Modifier
//                                        .fillMaxSize()
//                                        .padding(vertical = 16.dp),
//                                    verticalArrangement = Arrangement.SpaceBetween
//                                ) {
//                                    NotTransactionTextActivities(
//                                        text1 = "You added ",
//                                        text2 = "Simple work space",
//                                        text3 = " to Your Favorite"
//                                    )
//                                    Text(
//                                        text = "2 hours ago",
//                                        fontSize = 14.sp,
//                                        color = Color.Gray
//                                    )
//                                }
//                            }
//                        }
//
//                        Card(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(100.dp)
//                                .padding(horizontal = 8.dp),
//                            shape = RoundedCornerShape(10.dp),
//                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
//                            colors = CardDefaults.cardColors(containerColor = Color.White)
//                        ) {
//                            Row(
//                                modifier = Modifier.fillMaxSize(),
//                                verticalAlignment = Alignment.CenterVertically,
//                            ) {
//                                AsyncImage(
//                                    model = "https://media.licdn.com/dms/image/D5612AQFxDB38HiHngQ/article-cover_image-shrink_600_2000/0/1703142367412?e=2147483647&v=beta&t=KTZakpTszD-neJALKJqG0lvWq6DRmD6wHZ9g_Djhuqg",
//                                    contentDescription = null,
//                                    modifier = Modifier
//                                        .size(100.dp)
//                                        .padding(horizontal = 16.dp)
//                                        .aspectRatio(1f),
//                                    contentScale = ContentScale.FillBounds
//                                )
//                                Column(
//                                    modifier = Modifier
//                                        .fillMaxSize()
//                                        .padding(vertical = 16.dp),
//                                    verticalArrangement = Arrangement.SpaceBetween
//                                ) {
//                                    NotTransactionTextActivities(
//                                        text1 = "You added ",
//                                        text2 = "Simple work space",
//                                        text3 = " to Your Favorite"
//                                    )
//                                    Text(
//                                        text = "2 hours ago",
//                                        fontSize = 14.sp,
//                                        color = Color.Gray
//                                    )
//                                }
//                            }
//                        }
//
//                        Card(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(100.dp)
//                                .padding(horizontal = 8.dp),
//                            shape = RoundedCornerShape(10.dp),
//                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
//                            colors = CardDefaults.cardColors(containerColor = Color.White)
//                        ) {
//                            Row(
//                                modifier = Modifier.fillMaxSize(),
//                                verticalAlignment = Alignment.CenterVertically,
//                            ) {
//                                AsyncImage(
//                                    model = "https://media.licdn.com/dms/image/D5612AQFxDB38HiHngQ/article-cover_image-shrink_600_2000/0/1703142367412?e=2147483647&v=beta&t=KTZakpTszD-neJALKJqG0lvWq6DRmD6wHZ9g_Djhuqg",
//                                    contentDescription = null,
//                                    modifier = Modifier
//                                        .size(100.dp)
//                                        .padding(horizontal = 16.dp)
//                                        .aspectRatio(1f),
//                                    contentScale = ContentScale.FillBounds
//                                )
//                                Column(
//                                    modifier = Modifier
//                                        .fillMaxSize()
//                                        .padding(vertical = 16.dp),
//                                    verticalArrangement = Arrangement.SpaceBetween
//                                ) {
//                                    NotTransactionTextActivities(
//                                        text1 = "You added ",
//                                        text2 = "Simple work space",
//                                        text3 = " to Your Favorite"
//                                    )
//                                    Text(
//                                        text = "2 hours ago",
//                                        fontSize = 14.sp,
//                                        color = Color.Gray
//                                    )
//                                }
//                            }
//                        }
//
//                        Card(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(100.dp)
//                                .padding(horizontal = 8.dp),
//                            shape = RoundedCornerShape(10.dp),
//                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
//                            colors = CardDefaults.cardColors(containerColor = Color.White)
//                        ) {
//                            Row(
//                                modifier = Modifier.fillMaxSize(),
//                                verticalAlignment = Alignment.CenterVertically,
//                            ) {
//                                AsyncImage(
//                                    model = "https://media.licdn.com/dms/image/D5612AQFxDB38HiHngQ/article-cover_image-shrink_600_2000/0/1703142367412?e=2147483647&v=beta&t=KTZakpTszD-neJALKJqG0lvWq6DRmD6wHZ9g_Djhuqg",
//                                    contentDescription = null,
//                                    modifier = Modifier
//                                        .size(100.dp)
//                                        .padding(horizontal = 16.dp)
//                                        .aspectRatio(1f),
//                                    contentScale = ContentScale.FillBounds
//                                )
//                                Column(
//                                    modifier = Modifier
//                                        .fillMaxSize()
//                                        .padding(vertical = 16.dp),
//                                    verticalArrangement = Arrangement.SpaceBetween
//                                ) {
//                                    NotTransactionTextActivities(
//                                        text1 = "You added ",
//                                        text2 = "Simple work space",
//                                        text3 = " to Your Favorite"
//                                    )
//                                    Text(
//                                        text = "2 hours ago",
//                                        fontSize = 14.sp,
//                                        color = Color.Gray
//                                    )
//                                }
//                            }
//                        }
//                    }
                }
            }
        }
    }

}

@Composable
private fun NotTransactionTextActivities(
    text1: String,
    text2: String,
    text3: String
) {
    Text(buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.DarkGray, fontSize = 15.sp)) {
            append(text1)
        }
        withStyle(
            style = SpanStyle(
                color = Color.DarkGray,
                fontSize = 15.sp,
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(text2)
        }
        withStyle(style = SpanStyle(color = Color.DarkGray, fontSize = 15.sp)) {
            append(text3)
        }
    })
}

@Composable
private fun TransactionTextActivities(
    text1: String,
    text2: String,
    text3: String
) {
    Text(buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = Color.DarkGray,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(text1)
        }
        withStyle(
            style = SpanStyle(
                color = Color.DarkGray,
                fontSize = 15.sp
            )
        ) {
            append(text2)
        }
        withStyle(
            style = SpanStyle(
                color = Color.DarkGray,
                fontSize = 15.sp,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(text3)
        }
    })
}