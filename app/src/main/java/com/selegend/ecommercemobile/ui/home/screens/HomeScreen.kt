@file:OptIn(ExperimentalMaterial3Api::class)

package com.selegend.ecommercemobile.ui.home.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.selegend.ecommercemobile.ui.auth.AuthViewModel
import com.selegend.ecommercemobile.ui.components.SuggestedProduct
import com.selegend.ecommercemobile.ui.home.events.CoreEvent
import com.selegend.ecommercemobile.ui.home.events.SearchProductEvent
import com.selegend.ecommercemobile.ui.home.viewmodels.CoreViewModel
import com.selegend.ecommercemobile.ui.home.viewmodels.ProductTypeViewModel
import com.selegend.ecommercemobile.ui.home.viewmodels.SearchViewModel
import com.selegend.ecommercemobile.ui.utils.UIEvent
import kotlinx.coroutines.flow.merge

@Composable
fun HomeScreen(
    onNavigate: (UIEvent.Navigate) -> Unit,
    coreViewModel: CoreViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = hiltViewModel(),
    productTypeViewModel: ProductTypeViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel()
) {

    val suggestedProduct by searchViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        merge(
            coreViewModel.uiEvent,
            searchViewModel.uiEvent,
            productTypeViewModel.uiEvent
        ).collect { event ->
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
                    onQueryChange = {
                        text = it
                        searchViewModel.onEvent(SearchProductEvent.OnSearchQueryChange(it))
                    },
                    onSearch = {
//                        items.add(text)
//                        active = false
                        searchViewModel.onEvent(SearchProductEvent.OnSubmitSearch(text))
                    },
                    colors = SearchBarDefaults.colors(
                        containerColor = Color.White,
                        inputFieldColors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    ),
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
//                    items.forEach {
//                        Row(
//                            modifier = Modifier
//                                .padding(all = 14.dp)
//                        ) {
//                            Icon(
//                                modifier = Modifier.padding(end = 10.dp),
//                                imageVector = Icons.Default.History,
//                                contentDescription = "History Icon"
//                            )
//                            Text(text = it)
//                        }
//                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 56.dp)
                    ) {
                        Text(
                            text = "Search suggestions",
                            style = MaterialTheme.typography.bodyMedium,
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(8.dp)
                        )
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(8.dp)
                        ) {
                            suggestedProduct.products?.let { it ->
                                items(it) {
                                    SuggestedProduct(
                                        product = it,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(120.dp)
                                            .border(1.dp, MaterialTheme.colorScheme.surfaceVariant)
                                            .clickable {
                                                searchViewModel.onEvent(
                                                    SearchProductEvent.OnProductClick(
                                                        it.id
                                                    )
                                                )
                                            }
                                    )
                                }

                            }
                        }
                    }

                }
            }
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(10.dp),
                containerColor = Color.White
            ) {
                BottomNavigationItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home Icon") },
                    label = { Text("Home") },
                    selected = true,
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
                        icon = { Icon(Icons.Default.Person, contentDescription = "Person Icon") },
                        label = { Text("Me") },
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
                    ProductTypeSession(
                        onEvent = productTypeViewModel::onEvent,
                        state = productTypeViewModel.smartphoneState
                    )
                    ProductTypeSession(
                        onEvent = productTypeViewModel::onEvent,
                        state = productTypeViewModel.laptopState
                    )
                    ProductTypeSession(
                        onEvent = productTypeViewModel::onEvent,
                        state = productTypeViewModel.accessoryState
                    )
                    ProductTypeSession(
                        onEvent = productTypeViewModel::onEvent,
                        state = productTypeViewModel.cameraState
                    )
                    ProductTypeSession(
                        onEvent = productTypeViewModel::onEvent,
                        state = productTypeViewModel.pcState
                    )
                    ProductTypeSession(
                        onEvent = productTypeViewModel::onEvent,
                        state = productTypeViewModel.studioState
                    )
                    ProductTypeSession(
                        onEvent = productTypeViewModel::onEvent,
                        state = productTypeViewModel.tvState
                    )
//                    LaptopScreen(
//                        modifier = Modifier.fillMaxSize(),
//                        onNavigate = onNavigate
//                    )
//                    AccessoryScreen(
//                        modifier = Modifier.fillMaxSize(),
//                        onNavigate = onNavigate
//                    )
//                    CameraScreen(
//                        modifier = Modifier.fillMaxSize(),
//                        onNavigate = onNavigate
//                    )
//                    PCScreen(
//                        modifier = Modifier.fillMaxSize(),
//                        onNavigate = onNavigate
//                    )
//                    StudioScreen(
//                        modifier = Modifier.fillMaxSize(),
//                        onNavigate = onNavigate
//                    )
//                    TVScreen(
//                        modifier = Modifier.fillMaxSize(),
//                        onNavigate = onNavigate
//                    )
                }
            }
        }
    }
}

fun Modifier.badgeLayout() =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)

        // based on the expectation of only one line of text
        val minPadding = placeable.height / 4

        val width = maxOf(placeable.width + minPadding, placeable.height) / 2
        layout(width, placeable.height / 2) {
            placeable.place((width - placeable.width) / 2, -placeable.height / 4)
        }
    }