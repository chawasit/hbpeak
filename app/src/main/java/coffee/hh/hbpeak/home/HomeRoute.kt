package coffee.hh.hbpeak.home

import androidx.compose.runtime.Composable

@Composable
fun HomeRoute(
    onNavUp: () -> Unit,
){
    HomeScreen(onNavUp = onNavUp)
}

