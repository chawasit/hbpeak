package coffee.hh.hbpeak

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coffee.hh.hbpeak.Destinations.HOME_ROUTE
import coffee.hh.hbpeak.Destinations.SIGN_IN_ROUTE
import coffee.hh.hbpeak.Destinations.SIGN_UP_ROUTE
import coffee.hh.hbpeak.Destinations.WELCOME_ROUTE
import coffee.hh.hbpeak.home.HomeRoute
import coffee.hh.hbpeak.signinsignup.route.SignInRoute
import coffee.hh.hbpeak.signinsignup.route.SignUpRoute
import coffee.hh.hbpeak.signinsignup.route.WelcomeRoute

object Destinations {
    const val WELCOME_ROUTE = "welcome"
    const val SIGN_UP_ROUTE = "signup/{email}"
    const val SIGN_IN_ROUTE = "signin/{email}"
    const val HOME_ROUTE = "home"
}

@Composable
fun HBPeakNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = WELCOME_ROUTE,

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
                onNavUp = navController::navigateUp
            )
        }
    }
}
