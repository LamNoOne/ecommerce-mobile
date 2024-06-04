@file:OptIn(ExperimentalFoundationApi::class)

package com.selegend.ecommercemobile.ui.orders

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.selegend.ecommercemobile.store.domain.model.core.orders.OrderHistory
import com.selegend.ecommercemobile.ui.components.LoadingItem
import com.selegend.ecommercemobile.ui.orders.components.OrderHistoryCard
import com.selegend.ecommercemobile.ui.utils.UIEvent

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OrdersScreen(
    onPopBackStack: () -> Boolean,
    onNavigate: (UIEvent.Navigate) -> Unit,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
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

    val allOrderData: LazyPagingItems<OrderHistory> =
        viewModel.allOrderPager.collectAsLazyPagingItems()
    val pendingOrderData: LazyPagingItems<OrderHistory> =
        viewModel.pendingOrderPager.collectAsLazyPagingItems()
    val paidOrderData: LazyPagingItems<OrderHistory> =
        viewModel.paidOrderPager.collectAsLazyPagingItems()
    val deliveringOrderData: LazyPagingItems<OrderHistory> =
        viewModel.deliveringOrderPager.collectAsLazyPagingItems()
    val deliveredOrderData: LazyPagingItems<OrderHistory> =
        viewModel.deliveredOrderPager.collectAsLazyPagingItems()

    if (
        allOrderData.loadState.refresh is LoadState.Loading
    ) {
        LoadingItem()
    }

    val tabItems = listOf(
        TabItem(
            title = "All",
            orders = allOrderData
        ),
        TabItem(
            title = "Pending",
            orders = pendingOrderData
        ),
        TabItem(
            title = "Paid",
            orders = paidOrderData
        ),
        TabItem(
            title = "Delivering",
            orders = deliveringOrderData
        ),
        TabItem(
            title = "Delivered",
            orders = deliveredOrderData
        )
    )

    var selectedTabIndex by remember {
        mutableStateOf(0)
    }

    val pagerState = rememberPagerState {
        tabItems.size
    }

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedTabIndex = pagerState.currentPage
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        topBar = {
            androidx.compose.material.TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                backgroundColor = Color.Transparent,
                elevation = 0.dp,
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            onPopBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                    Text(
                        text = "My Orders",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { viewModel.onEvent(OrdersEvent.OnBackHome) }) {
                            Icon(
                                imageVector = Icons.Outlined.Home,
                                contentDescription = "Search Button"
                            )
                        }
                    }
                }
            }
        }
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
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.Transparent,
                edgePadding = 0.dp,
            ) {
                tabItems.forEachIndexed { index, tabItem ->
                    Tab(
                        selected = index == selectedTabIndex,
                        onClick = {
                            selectedTabIndex = index
                        },
                        text = {
                            Text(text = tabItem.title)
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) { index ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
                        .padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(tabItems[index].orders.itemCount) { indexCount ->
                        OrderHistoryCard(
                            modifier = Modifier
                                .shadow(
                                    elevation = 24.dp,
                                    spotColor = Color(0x1F000000),
                                    ambientColor = Color(0x1F000000)
                                )
                                .fillMaxWidth()
                                .height(164.dp)
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(size = 8.dp)
                                )
                                .padding(16.dp),
                            orderState = tabItems[index].orders[indexCount]!!,
                            onEvent = viewModel::onEvent
                        )
                    }
                }
            }
        }
    }
}

data class TabItem(
    val title: String,
    val orders: LazyPagingItems<OrderHistory>
)