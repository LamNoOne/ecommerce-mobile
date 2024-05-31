@file:OptIn(ExperimentalMaterial3Api::class)

package com.selegend.ecommercemobile.ui.products

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.selegend.ecommercemobile.store.domain.model.core.products.Product
import com.selegend.ecommercemobile.ui.components.ErrorItem
import com.selegend.ecommercemobile.ui.components.LoadingItem
import com.selegend.ecommercemobile.ui.components.SuggestedProduct
import com.selegend.ecommercemobile.ui.home.components.ProductCard
import com.selegend.ecommercemobile.ui.home.events.SearchProductEvent
import com.selegend.ecommercemobile.ui.home.viewmodels.SearchViewModel
import com.selegend.ecommercemobile.ui.products.components.SortButton
import com.selegend.ecommercemobile.ui.utils.UIEvent
import kotlinx.coroutines.flow.merge

@Composable
fun ProductsScreen(
    onNavigate: (UIEvent.Navigate) -> Unit,
    onPopBackStack: () -> Unit,
    viewModel: ProductsViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = hiltViewModel(),
) {
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = true) {
        merge(viewModel.uiEvent, searchViewModel.uiEvent).collect { event ->
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

    val productsData: LazyPagingItems<Product> = viewModel.productPager.collectAsLazyPagingItems()
    val selectionSortState by viewModel.selected.collectAsStateWithLifecycle()

    val suggestedProduct by searchViewModel.state.collectAsStateWithLifecycle()
    var active by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(viewModel.search) }


    if (productsData.loadState.refresh is LoadState.Loading) {
        LoadingItem()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
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
                    Column(modifier = Modifier.fillMaxSize()) {
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
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = it.calculateTopPadding(),
                    bottom = it.calculateBottomPadding(),
                    start = 8.dp,
                    end = 8.dp
                )
        ) {
            val buttonSortSize: Dp = (LocalConfiguration.current.screenWidthDp / 3 - 16).dp
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SortButton(
                    buttonSortSize = buttonSortSize,
                    onClick = {
                        viewModel.onEvent(ProductsEvent.OnBestMatchSelected())
                        productsData.refresh()
                    },
                    text = "Best Match",
                    active = selectionSortState.isBestMatch
                )
                SortButton(
                    buttonSortSize = buttonSortSize,
                    onClick = {
                        viewModel.onEvent(ProductsEvent.OnPriceLowToHighSelected())
                        productsData.refresh()
                    },
                    text = "Low - High",
                    active = selectionSortState.isPriceLowToHigh
                )
                SortButton(
                    buttonSortSize = buttonSortSize,
                    onClick = {
                        viewModel.onEvent(ProductsEvent.OnPriceHighToLowSelected())
                        productsData.refresh()
                    },
                    text = "High - Low",
                    active = selectionSortState.isPriceHighToLow
                )
            }

            HorizontalDivider(thickness = 14.dp, color = Color.Transparent)

            val itemSize: Dp = (LocalConfiguration.current.screenWidthDp / 2 - 11).dp
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(productsData.itemCount) { index ->
                    ProductCard(
                        modifier = Modifier
                            .width(itemSize)
                            .fillMaxHeight()
                            .clickable {
                                viewModel.onEvent(ProductsEvent.OnProductClick(productsData[index]!!.id))
                            },
                        product = productsData[index]!!
                    )
                }
                when (productsData.loadState.append) {
                    is LoadState.NotLoading -> Unit
                    is LoadState.Loading -> {
                        item {
                            LoadingItem()
                        }
                    }
                    is LoadState.Error -> {
                        item {
                            ErrorItem(message = (productsData.loadState.append as LoadState.Error).error.message.toString())
                        }
                    }
                }
            }
        }
    }
}