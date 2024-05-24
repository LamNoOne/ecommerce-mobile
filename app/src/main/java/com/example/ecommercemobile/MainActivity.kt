package com.example.ecommercemobile

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.ecommercemobile.ui.auth.AuthScreen
import com.example.ecommercemobile.ui.cart.CartScreen
import com.example.ecommercemobile.ui.checkout.CheckoutScreen
import com.example.ecommercemobile.ui.favorite.FavoriteScreen
import com.example.ecommercemobile.ui.home.screens.HomeScreen
import com.example.ecommercemobile.ui.product_detail.ProductDetailScreen
import com.example.ecommercemobile.ui.products.ProductsScreen
import com.example.ecommercemobile.ui.profile.ProfileScreen
import com.example.ecommercemobile.ui.theme.EcommerceMobileTheme
import com.example.ecommercemobile.utils.Event
import com.example.ecommercemobile.utils.EventBus
import com.example.ecommercemobile.utils.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcommerceMobileTheme {
                // A surface container using the 'background' color from the theme
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
}