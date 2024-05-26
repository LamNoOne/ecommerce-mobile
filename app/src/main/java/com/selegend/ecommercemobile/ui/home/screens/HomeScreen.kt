@file:OptIn(ExperimentalMaterial3Api::class)

package com.selegend.ecommercemobile.ui.home.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.selegend.ecommercemobile.ui.auth.AuthViewModel
import com.selegend.ecommercemobile.ui.home.events.CoreEvent
import com.selegend.ecommercemobile.ui.home.viewmodels.CoreViewModel
import com.selegend.ecommercemobile.ui.utils.UIEvent

@Composable
fun HomeScreen(
    onNavigate: (UIEvent.Navigate) -> Unit,
    coreViewModel: CoreViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        coreViewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }

    var text by remember { mutableStateOf("") }

    var active by remember { mutableStateOf(false) }

    var items = remember { mutableStateListOf("") }

    Scaffold(modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = if (active) 0.dp else 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchBar(
                    modifier = if (!active) Modifier
                        .padding(bottom = 12.dp)
                        .weight(1f)
                    else Modifier.weight(1f),
                    shape = SearchBarDefaults.fullScreenShape,
                    query = text,
                    onQueryChange = { text = it },
                    onSearch = {
                        items.add(text)
                        active = false
                    },
                    active = active,
                    onActiveChange = { active = it },
                    placeholder = { Text("Search") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                    },
                    trailingIcon = {
                        if (active) {
                            Icon(modifier = Modifier.clickable {
                                if (text.isNotEmpty()) {
                                    text = ""
                                } else {
                                    active = false
                                }
                            }, imageVector = Icons.Default.Close, contentDescription = "Close Icon")
                        }
                    }
                ) {
                    items.forEach {
                        Row(
                            modifier = Modifier
                                .padding(all = 14.dp)
                                .zIndex(1f)
                        ) {
                            Icon(
                                modifier = Modifier.padding(end = 10.dp),
                                imageVector = Icons.Default.History,
                                contentDescription = "History Icon"
                            )
                            Text(text = it)
                        }
                    }
                }
                if (!active) {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Shopping Cart Icon"
                        )
                    }
                }
            }
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home Icon") },
                    label = { Text("Home") },
                    selected = true,
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
                        selected = false,
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
        LazyVerticalStaggeredGrid(
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding()
            ),
            columns = StaggeredGridCells.Fixed(1),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalItemSpacing = 10.dp
        ) {
            item {
                Column(modifier = Modifier.fillMaxSize()) {
                    CategoryScreen(modifier = Modifier.fillMaxSize(), onNavigate = onNavigate)
                    SmartPhoneScreen(
                        modifier = Modifier.fillMaxSize(),
                        onNavigate = onNavigate
                    )
                    LaptopScreen(
                        modifier = Modifier.fillMaxSize(),
                        onNavigate = onNavigate
                    )
                    AccessoryScreen(
                        modifier = Modifier.fillMaxSize(),
                        onNavigate = onNavigate
                    )
                    CameraScreen(
                        modifier = Modifier.fillMaxSize(),
                        onNavigate = onNavigate
                    )
                    PCScreen(
                        modifier = Modifier.fillMaxSize(),
                        onNavigate = onNavigate
                    )
                    StudioScreen(
                        modifier = Modifier.fillMaxSize(),
                        onNavigate = onNavigate
                    )
                    TVScreen(
                        modifier = Modifier.fillMaxSize(),
                        onNavigate = onNavigate
                    )
                }
            }
        }
    }
}