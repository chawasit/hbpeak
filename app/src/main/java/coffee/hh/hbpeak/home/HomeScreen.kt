package coffee.hh.hbpeak.home

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import coffee.hh.hbpeak.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavUp: () -> Unit
) {
    val homeBarText: String = stringResource(R.string.home_bar)
    
    Scaffold(
        topBar = {
            HomeTopAppBar(
                topAppBarText = homeBarText,
                onNavUp = onNavUp
            ) },
        content = { contentPadding ->
            HomeContent(contentPadding = contentPadding)
        }
    )
}