package com.example.ecommercemobile

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ecommercemobile.ui.home.screens.HomeScreen
import com.example.ecommercemobile.ui.product_detail.ProductDetailScreen
import com.example.ecommercemobile.ui.theme.EcommerceMobileTheme
import com.example.ecommercemobile.utils.Event
import com.example.ecommercemobile.utils.EventBus
import com.example.ecommercemobile.utils.Routes
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcommerceMobileTheme {
                val systemUIController = rememberSystemUiController()
                val darkTheme = isSystemInDarkTheme()
                SideEffect {
                    systemUIController.setSystemBarsColor(
                        color = if(darkTheme) Color.LightGray else Color.White
                    )
                }
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
                Surface {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Routes.HOME ) {
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
                            ProductDetailScreen()
                        }
                    }
                }
            }
        }
    }
}