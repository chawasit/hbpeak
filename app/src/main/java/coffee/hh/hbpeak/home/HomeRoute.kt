package coffee.hh.hbpeak.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun HomeRoute(
    navController: NavHostController,
){
    HomeScreen(onNavUp = navController::navigateUp, navController)
}

