package com.cleansafi.presentation.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cleansafi.presentation.auth.login.LoginScreen
import com.cleansafi.presentation.auth.signup.SignupScreen
import com.cleansafi.presentation.cart.CartScreen
import com.cleansafi.presentation.checkout.CheckoutScreen
import com.cleansafi.presentation.home.HomeScreen
import com.cleansafi.presentation.order.placeorder.PlaceOrderScreen
import com.cleansafi.presentation.orders.OrdersScreen
import com.cleansafi.presentation.payment.PaymentScreen
import com.cleansafi.presentation.profile.ProfileScreen
import com.cleansafi.presentation.splash.SplashScreen

@Composable
fun AppNavigation(initialOrderId: Long? = null) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val networkMonitor = remember {
        com.cleansafi.core.network.NetworkMonitor(context.applicationContext)
    }

    // Deep link to orders screen if orderId provided
    androidx.compose.runtime.LaunchedEffect(initialOrderId) {
        if (initialOrderId != null) {
            navController.navigate("orders") { popUpTo("splash") { inclusive = false } }
        }
    }

    NavHost(
            navController = navController,
            startDestination = "splash",
            enterTransition = { fadeIn() + slideInHorizontally { it / 2 } },
            exitTransition = { fadeOut() + slideOutHorizontally { -it / 2 } },
            popEnterTransition = { fadeIn() + slideInHorizontally { -it / 2 } },
            popExitTransition = { fadeOut() + slideOutHorizontally { it / 2 } }
    ) {
        composable("splash") {
            SplashScreen(
                    onNavigateToOnboarding = {
                        navController.navigate("onboarding") {
                            popUpTo("splash") { inclusive = true }
                        }
                    },
                    onNavigateToAuth = {
                        navController.navigate("login") { popUpTo("splash") { inclusive = true } }
                    },
                    onNavigateToHome = {
                        navController.navigate("home") { popUpTo("splash") { inclusive = true } }
                    }
            )
        }

        composable("onboarding") {
            com.cleansafi.presentation.onboarding.OnboardingScreen(
                    onComplete = {
                        navController.navigate("login") {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    }
            )
        }

        composable("login") {
            LoginScreen(
                    onNavigateToSignup = { navController.navigate("signup") },
                    onNavigateToHome = {
                        navController.navigate("home") { popUpTo("login") { inclusive = true } }
                    },
                    onNavigateToForgotPassword = {
                        // TODO: Implement forgot password
                    }
            )
        }

        composable("signup") {
            SignupScreen(
                    onNavigateToLogin = { navController.popBackStack() },
                    onNavigateToHome = {
                        navController.navigate("home") { popUpTo("signup") { inclusive = true } }
                    }
            )
        }

        composable("home") {
            HomeScreen(
                    onNavigateToPlaceOrder = { navController.navigate("placeOrder") },
                    onNavigateToOrders = { navController.navigate("orders") },
                    onNavigateToProfile = { navController.navigate("profile") }
            )
        }

        composable("placeOrder") {
            PlaceOrderScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToCart = { navController.navigate("cart") },
                    networkMonitor = networkMonitor
            )
        }

        composable("cart") {
            CartScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToCheckout = { navController.navigate("checkout") },
                    networkMonitor = networkMonitor
            )
        }

        composable("checkout") {
            CheckoutScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToPayment = { orderId, amount ->
                        navController.navigate("payment/$orderId/$amount") {
                            popUpTo("checkout") { inclusive = true }
                        }
                    },
                    networkMonitor = networkMonitor
            )
        }

        composable("payment/{orderId}/{amount}") {
            PaymentScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onPaymentSuccess = {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    networkMonitor = networkMonitor
            )
        }

        composable("orders") { OrdersScreen(onNavigateBack = { navController.popBackStack() }) }

        composable("profile") {
            ProfileScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToLogin = {
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    },
                    networkMonitor = networkMonitor
            )
        }
    }
}
