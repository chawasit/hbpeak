package coffee.hh.hbpeak.roasting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import coffee.hh.hbpeak.MachineState
import coffee.hh.hbpeak.composable.TopAppBar

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoastingScreen(
    onNavUp: () -> Unit = {},
    machineState: MutableState<MachineState> = mutableStateOf(MachineState()),
    enqueueCommand: (String) -> Unit = fun(_) {},
    onToggleTheme: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                topAppBarText = "Roasting",
                onNavUp = onNavUp,
                onToggleTheme = onToggleTheme
            )
        },
        content = { contentPadding ->
            Box(modifier = Modifier.padding(contentPadding)) {
                RoastingContent(machineState, enqueueCommand)
            }
        }
    )
}

