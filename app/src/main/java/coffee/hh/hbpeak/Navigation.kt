package coffee.hh.hbpeak

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coffee.hh.hbpeak.Destinations.HOME_ROUTE
import coffee.hh.hbpeak.Destinations.ROASTING_ROUTE
import coffee.hh.hbpeak.Destinations.SIGN_IN_ROUTE
import coffee.hh.hbpeak.Destinations.SIGN_UP_ROUTE
import coffee.hh.hbpeak.Destinations.WELCOME_ROUTE
import coffee.hh.hbpeak.home.HomeRoute
import coffee.hh.hbpeak.roasting.RoastingRoute
import coffee.hh.hbpeak.signinsignup.route.SignInRoute
import coffee.hh.hbpeak.signinsignup.route.SignUpRoute
import coffee.hh.hbpeak.signinsignup.route.WelcomeRoute

@Suppress("unused")
object Destinations {
    const val WELCOME_ROUTE = "welcome"
    const val SIGN_UP_ROUTE = "signup/{email}"
    const val SIGN_IN_ROUTE = "signin/{email}"
    const val HOME_ROUTE = "home"
    const val ROASTING_ROUTE = "roasting"
    const val PROFILE_ROUTE = "profile"
    const val INVENTORY_ROUTE = "inventory"
    const val CUPPING_ROUTE = "cupping"
    const val ACCOUNT_ROUTE = "account"
    const val SETTINGS_ROUTE = "settings"
}

@Composable
fun HBPeakNavHost(
    machineState: MutableState<MachineState>,
    enqueueCommand: (String) -> Unit,
    onToggleTheme: () -> Unit,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = WELCOME_ROUTE,
        enterTransition = { fadeIn(tween(400)) },
        exitTransition = { fadeOut(tween(300)) }
    ) {
        composable(WELCOME_ROUTE) {
            WelcomeRoute(
                onNavigateToSignIn = {
                    navController.navigate("signin/$it")
                },
                onNavigateToSignUp = {
                    navController.navigate("signup/$it")
                },
                onSignInAsGuest = {
                    navController.navigate(HOME_ROUTE)
                },
            )
        }

        composable(SIGN_IN_ROUTE) {
            val startingEmail = it.arguments?.getString("email")
            SignInRoute(
                email = startingEmail,
                onSignInSubmitted = {
                    navController.navigate(HOME_ROUTE)
                },
                onSignInAsGuest = {
                    navController.navigate(HOME_ROUTE)
                },
                onNavUp = navController::navigateUp,
            )
        }

        composable(SIGN_UP_ROUTE) {
            val startingEmail = it.arguments?.getString("email")
            SignUpRoute(
                email = startingEmail,
                onSignUpSubmitted = {
                    navController.navigate(HOME_ROUTE)
                },
                onSignInAsGuest = {
                    navController.navigate(HOME_ROUTE)
                },
                onNavUp = navController::navigateUp,
            )
        }

        composable(HOME_ROUTE) {
            HomeRoute(
                navController = navController,
                onToggleTheme = onToggleTheme
            )
        }

        composable(ROASTING_ROUTE) {
            RoastingRoute(
                navController = navController,
                machineState = machineState,
                enqueueCommand = enqueueCommand,
                onToggleTheme = onToggleTheme
            )
        }
    }
}
