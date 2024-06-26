package coffee.hh.hbpeak.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun HomeRoute(
    navController: NavHostController,
    onToggleTheme: () -> Unit,
) {
    HomeScreen(
        onNavUp = navController::navigateUp,
        navController,
        onToggleTheme = onToggleTheme
    )
}

