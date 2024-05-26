package com.selegend.ecommercemobile

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.wallet.contract.TaskResultContracts
import com.selegend.ecommercemobile.ui.auth.AuthScreen
import com.selegend.ecommercemobile.ui.cart.CartScreen
import com.selegend.ecommercemobile.ui.checkout.CheckoutScreen
import com.selegend.ecommercemobile.ui.favorite.FavoriteScreen
import com.selegend.ecommercemobile.ui.home.screens.HomeScreen
import com.selegend.ecommercemobile.ui.payment.PaymentScreen
import com.selegend.ecommercemobile.ui.payment.PaymentViewModel
import com.selegend.ecommercemobile.ui.product_detail.ProductDetailScreen
import com.selegend.ecommercemobile.ui.products.ProductsScreen
import com.selegend.ecommercemobile.ui.profile.ProfileScreen
import com.selegend.ecommercemobile.ui.theme.EcommerceMobileTheme
import com.selegend.ecommercemobile.utils.Event
import com.selegend.ecommercemobile.utils.EventBus
import com.selegend.ecommercemobile.utils.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val paymentDataLauncher =
        registerForActivityResult(TaskResultContracts.GetPaymentDataResult()) { taskResult ->
            when (taskResult.status.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    taskResult.result!!.let {
                        Log.i("Google Pay result:", it.toJson())
                        model.setPaymentData(it)
                    }
                }
                //CommonStatusCodes.CANCELED -> The user canceled
                //AutoResolveHelper.RESULT_ERROR -> The API returned an error (it.status: Status)
                //CommonStatusCodes.INTERNAL_ERROR -> Handle other unexpected errors
            }
        }

    private val model: PaymentViewModel by viewModels()

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
                                    type = NavType.IntType
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
                            ProductsScreen(onNavigate = {
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
                            PaymentScreen(
                                onPopBackStack = { navController.popBackStack() },
                                onNavigate = {
                                    navController.navigate(it.route)
                                },
                                onGooglePayButtonClick = { requestPayment() }
                            )

                        }
                        composable(Routes.FAVORITE) {
                            FavoriteScreen()
                        }
                        composable(Routes.PROFILE) {
                            ProfileScreen()
                        }
                        composable(Routes.LOGIN) {
                            AuthScreen(
                                onPopBackStack = { navController.popBackStack() },
                                onNavigate = {
                                    navController.navigate(it.route)
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