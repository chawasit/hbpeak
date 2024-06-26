package coffee.hh.hbpeak.home

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import coffee.hh.hbpeak.Destinations
import coffee.hh.hbpeak.R
import coffee.hh.hbpeak.composable.TopAppBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavUp: () -> Unit,
    navController: NavHostController,
    onToggleTheme: () -> Unit,
) {
    val homeBarText: String = stringResource(R.string.home_bar)

    Scaffold(
        topBar = {
            TopAppBar(
                topAppBarText = homeBarText,
                onNavUp = onNavUp,
                onToggleTheme = onToggleTheme
            )
        },
        content = { contentPadding ->
            HomeContent(contentPadding = contentPadding, onRoastingClick = {
                navController.navigate(Destinations.ROASTING_ROUTE)
            })
        }
    )
}