package coffee.hh.hbpeak.roasting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coffee.hh.hbpeak.MachineState
import coffee.hh.hbpeak.R

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoastingScreen(
    onNavUp: () -> Unit = {},
    machineState: MutableState<MachineState> = mutableStateOf(MachineState()),
    enqueueCommand: (String) -> Unit = fun(_){},
) {
    Scaffold(
        topBar = { RoastingTopAppBar(topAppBarText = "Roasting", onNavUp = onNavUp) },
        content = { contentPadding ->
            Box(modifier = Modifier.padding(contentPadding)) {
                RoastingContent(machineState, enqueueCommand)
            }
        }
    )
}

@Preview
@ExperimentalMaterial3Api
@Composable
fun RoastingTopAppBar(
    topAppBarText: String = "Roasting",
    onNavUp: () -> Unit = {},
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