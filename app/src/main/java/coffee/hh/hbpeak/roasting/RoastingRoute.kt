package coffee.hh.hbpeak.roasting

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coffee.hh.hbpeak.MachineState
import coffee.hh.hbpeak.R


@Composable
fun RoastingRoute(
    navController: NavHostController,
    machineState: MachineState,
    enqueueCommand: (String) -> Unit,
) {
    RoastingContent(
        onNavUp = navController::navigateUp,
        machineState = machineState,
        enqueueCommand = enqueueCommand,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoastingContent(
    onNavUp: () -> Unit,
    machineState: MachineState,
    enqueueCommand: (String) -> Unit,
) {
    Scaffold(
        topBar = { RoastingTopAppBar(topAppBarText = "Roasting", onNavUp = onNavUp) },
        content = { contentPadding ->
            RoastingContent(contentPadding, machineState, enqueueCommand)
        }
    )
}


@ExperimentalMaterial3Api
@Composable
fun RoastingTopAppBar(
    topAppBarText: String,
    onNavUp: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = topAppBarText,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavUp) {
                Icon(
                    imageVector = Icons.Filled.ChevronLeft,
                    contentDescription = stringResource(id = R.string.back),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        // We need to balance the navigation icon, so we add a spacer.
        actions = {
            Spacer(modifier = Modifier.width(68.dp))
        },
    )
}