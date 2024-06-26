package com.selegend.ecommercemobile

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.wallet.contract.TaskResultContracts
import com.selegend.ecommercemobile.ui.auth.AuthScreen
import com.selegend.ecommercemobile.ui.cart.CartScreen
import com.selegend.ecommercemobile.ui.checkout.CheckoutScreen
import com.selegend.ecommercemobile.ui.home.screens.HomeScreen
import com.selegend.ecommercemobile.ui.order_detail.OrderDetailScreen
import com.selegend.ecommercemobile.ui.orders.OrdersScreen
import com.selegend.ecommercemobile.ui.payment.PaymentScreen
import com.selegend.ecommercemobile.ui.payment.PaymentUiState
import com.selegend.ecommercemobile.ui.payment.PaymentViewModel
import com.selegend.ecommercemobile.ui.payment.TransactionViewState
import com.selegend.ecommercemobile.ui.product_detail.ProductDetailScreen
import com.selegend.ecommercemobile.ui.products.ProductsScreen
import com.selegend.ecommercemobile.ui.profile.ProfileScreen
import com.selegend.ecommercemobile.ui.theme.EcommerceMobileTheme
import com.selegend.ecommercemobile.ui.user.UserScreen
import com.selegend.ecommercemobile.ui.user.UserViewModel
import com.selegend.ecommercemobile.utils.Event
import com.selegend.ecommercemobile.utils.EventBus
import com.selegend.ecommercemobile.utils.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    var orderId: Int = 0
    val applicationContext = this

    @JvmName("setOrderId1")
    fun setOrderId(orderId: Int) {
        this.orderId = orderId
    }

    private val paymentDataLauncher =
        registerForActivityResult(TaskResultContracts.GetPaymentDataResult()) { taskResult ->
            when (taskResult.status.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    taskResult.result!!.let {
                        Log.i("Google Pay result:", it.toJson())
                        model.setPaymentData(it, orderId = orderId)
                    }
                }
                //CommonStatusCodes.CANCELED -> The user canceled
                //AutoResolveHelper.RESULT_ERROR -> The API returned an error (it.status: Status)
                //CommonStatusCodes.INTERNAL_ERROR -> Handle other unexpected errors
            }
        }

    private val model: PaymentViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcommerceMobileTheme {
                /******* A surface container using the 'background' color from the theme ********/
                val lifecycle = LocalLifecycleOwner.current.lifecycle
                LaunchedEffect(key1 = lifecycle) {
                    repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                        EventBus.events.collect { event ->
                            if (event is Event.Toast) {
                                Toast
                                    .makeText(
                                        this@MainActivity,
                                        event.message,
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        }
                    }
                }
                /*********************************************************************************/

                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Routes.HOME) {
                        composable(Routes.HOME) {
                            HomeScreen(
                                onNavigate = {
                                    navController.navigate(it.route)
                                }
                            )
                        }
                        composable(
                            route = "${Routes.PRODUCT_DETAIL}?productId={productId}",
                            arguments = listOf(
                                navArgument("productId") {
                                    type = NavType.IntType
                                }
                            )
                        ) {
                            ProductDetailScreen(
                                onPopBackStack = { navController.popBackStack() },
                                onNavigate = {
                                    navController.navigate(it.route)
                                })
                        }
                        composable(
                            route = "${Routes.PRODUCT}?categoryId={categoryId}&search={search}&page={page}&limit={limit}&sortBy={sortBy}&order={order}",
                            arguments = listOf(
                                navArgument("categoryId") {
                                    nullable = true
                                },
                                navArgument("search") {
                                    nullable = true
                                },
                                navArgument("page") {
                                    nullable = true
                                },
                                navArgument("limit") {
                                    nullable = true
                                },
                                navArgument("sortBy") {
                                    nullable = true
                                },
                                navArgument("order") {
                                    nullable = true
                                }
                            )
                        ) {
                            ProductsScreen(
                                onPopBackStack = { navController.popBackStack() },
                                onNavigate = {
                                    navController.navigate(it.route)
                                })
                        }
                        composable(Routes.CART) {
                            CartScreen(
                                onPopBackStack = { navController.popBackStack() },
                                onNavigate = {
                                    navController.navigate(it.route)
                                })
                        }
                        composable(
                            route = "${Routes.CHECKOUT}?productIds={productIds}",
                            arguments = listOf(
                                navArgument("productIds") {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            CheckoutScreen(
                                onPopBackStack = { navController.popBackStack() },
                                onNavigate = {
                                    navController.navigate(it.route)
                                }
                            )
                        }
                        composable(
                            route = "${Routes.PAYMENT}?orderId={orderId}",
                            arguments = listOf(
                                navArgument("orderId") {
                                    type = NavType.IntType
                                }
                            )
                        ) {
                            val payState: PaymentUiState by model.paymentUiState.collectAsStateWithLifecycle()
                            val transactionState: TransactionViewState by model.transactionState.collectAsStateWithLifecycle()
                            PaymentScreen(
                                onPopBackStack = { navController.popBackStack() },
                                onNavigate = {
                                    navController.navigate(it.route)
                                },
                                payUiState = payState,
                                transactionState = transactionState,
                                onGooglePayButtonClick = { requestPayment() },
                                setOrderId = { setOrderId(it) }
                            )
                        }
                        navigation(
                            startDestination = Routes.MANAGE_USER,
                            route = Routes.USER
                        ) {
                            composable(Routes.MANAGE_USER) {entry ->
                                val viewModel = entry.sharedViewModel<UserViewModel>(navController)
                                val state by viewModel.state.collectAsStateWithLifecycle()
                                UserScreen(
                                    sharedState = state,
                                    onPopBackStack = { navController.popBackStack() },
                                    onNavigate = {
                                        navController.navigate(it.route)
                                    },
                                    onBackLogin = {
                                        navController.navigate(Routes.LOGIN) {
                                            popUpTo(Routes.MANAGE_USER) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                )
                            }
                            composable(Routes.PROFILE) {entry ->
                                val viewModel = entry.sharedViewModel<UserViewModel>(navController)
                                val state by viewModel.state.collectAsStateWithLifecycle()
                                ProfileScreen(
                                    sharedState = state,
                                    onPopBackStack = { navController.popBackStack() },
                                    onNavigate = {
                                        navController.navigate(it.route)
                                    },
                                    context = applicationContext
                                )
                            }
                        }
                        navigation(
                            startDestination = Routes.ORDERS,
                            route = Routes.ORDER_HISTORY
                        ) {
                            composable(Routes.ORDERS) {
                                OrdersScreen(
                                    onPopBackStack = { navController.popBackStack() },
                                    onNavigate = {
                                        navController.navigate(it.route)
                                    }
                                )
                            }
                            composable(
                                route = "${Routes.ORDER_DETAIL}?transactionId={transactionId}",
                                arguments = listOf(
                                    navArgument("transactionId") {
                                        type = NavType.StringType
                                    }
                                )) {
                                OrderDetailScreen(
                                    onPopBackStack = { navController.popBackStack() },
                                    onNavigate = {
                                        navController.navigate(it.route)
                                    }
                                )
                            }
                        }
                        composable(Routes.LOGIN) {
                            AuthScreen(
                                onPopBackStack = { navController.popBackStack() },
                                onNavigate = {
                                    navController.navigate(it.route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            inclusive = true
                                            saveState = false
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun requestPayment() {
        val task = model.getLoadPaymentDataTask(priceCents = 1000L)
        task.addOnCompleteListener(paymentDataLauncher::launch)
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}