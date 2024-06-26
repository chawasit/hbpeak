package coffee.hh.hbpeak.roasting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import coffee.hh.hbpeak.MachineState


@Composable
fun RoastingRoute(
    navController: NavHostController,
    machineState: MutableState<MachineState>,
    enqueueCommand: (String) -> Unit,
    onToggleTheme: () -> Unit,
) {
    RoastingScreen(
        onNavUp = navController::navigateUp,
        machineState = machineState,
        enqueueCommand = enqueueCommand,
        onToggleTheme = onToggleTheme
    )
}


