package coffee.hh.hbpeak.roasting

import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coffee.hh.hbpeak.MachineControlUnitIds
import coffee.hh.hbpeak.MachineNodeStatus
import coffee.hh.hbpeak.MachineState
import coffee.hh.hbpeak.MachineStateInterpreter
import coffee.hh.hbpeak.composable.NumberPadDialog
import coffee.hh.hbpeak.composable.SwitchWithLabel
import coffee.hh.hbpeak.theme.HBPeakTheme
import kotlinx.coroutines.launch

@SuppressLint("DefaultLocale")
@Composable
fun RoastingContent(
    machineState: MutableState<MachineState> = mutableStateOf(MachineState()),
    enqueueCommand: (String) -> Unit = fun(_) {},
    roastingGraphViewModel: RoastingGraphViewModel = viewModel()
) {
    val showDialog = remember { mutableStateOf(false) }
    val showTurnOffButton = remember { mutableStateOf(false) }
    val maxValue = remember { mutableIntStateOf(100) }
    val minValue = remember { mutableIntStateOf(0) }
    val currentField = remember { mutableStateOf("") }
    val currentValue = remember { mutableStateOf("") }
    val isStartRoasting = remember {
        mutableStateOf(false)
    }
    val coroutineScope = rememberCoroutineScope()

    when {
        showDialog.value -> {
            NumberPadDialog(
                title = "Set " + currentField.value,
                initialValue = currentValue.value,
                minValue = minValue.intValue,
                maxValue = maxValue.intValue,
                showTurnOff = showTurnOffButton.value,
                onDismissRequest = { showDialog.value = false },
                onValueConfirm = { value ->
                    when (currentField.value) {
                        "Drum RPM" -> {
                            coroutineScope.launch {
                                val command = MachineStateInterpreter.generateControlCommand(
                                    machineState.value,
                                    MachineControlUnitIds.DRUM_RPM,
                                    if (value.toInt() == 0) MachineNodeStatus.OFF else MachineNodeStatus.ON,
                                    value.toInt()
                                )
                                enqueueCommand(command)
                            }
                        }

                        "Air Speed" -> {
                            coroutineScope.launch {
                                val command = MachineStateInterpreter.generateControlCommand(
                                    machineState.value,
                                    MachineControlUnitIds.FAN_LEVEL,
                                    if (value.toInt() == 0) MachineNodeStatus.OFF else MachineNodeStatus.ON,
                                    value.toInt()
                                )
                                enqueueCommand(command)
                            }
                        }

                        "Preheat Temperature" -> {
                            coroutineScope.launch {
                                val command = MachineStateInterpreter.generateControlCommand(
                                    machineState.value,
                                    MachineControlUnitIds.PREHEAT_TEMPERATURE,
                                    if (value.toInt() == 0) MachineNodeStatus.OFF else MachineNodeStatus.ON,
                                    value.toInt()
                                )
                                enqueueCommand(command)
                            }
                        }

                        "Gas Level" -> {
                            coroutineScope.launch {
                                val command = MachineStateInterpreter.generateControlCommand(
                                    machineState.value,
                                    MachineControlUnitIds.GAS_LEVEL,
                                    if (value.toInt() == 0) MachineNodeStatus.OFF else MachineNodeStatus.ON,
                                    value.toInt()
                                )
                                enqueueCommand(command)
                            }
                        }
                    }
                    showDialog.value = false
                },
                onTurnOff = { value ->
                    when (currentField.value) {
                        "Drum RPM" -> {
                            coroutineScope.launch {
                                val command = MachineStateInterpreter.generateControlCommand(
                                    machineState.value,
                                    MachineControlUnitIds.DRUM_RPM,
                                    MachineNodeStatus.OFF,
                                    value.toInt()
                                )
                                enqueueCommand(command)
                            }
                        }

                        "Air Speed" -> {
                            coroutineScope.launch {
                                val command = MachineStateInterpreter.generateControlCommand(
                                    machineState.value,
                                    MachineControlUnitIds.FAN_LEVEL,
                                    MachineNodeStatus.OFF,
                                    value.toInt()
                                )
                                enqueueCommand(command)
                            }
                        }

                        "Preheat Temperature" -> {
                            coroutineScope.launch {
                                val offPreheatCommand =
                                    MachineStateInterpreter.generateControlCommand(
                                        machineState.value,
                                        MachineControlUnitIds.PREHEAT_TEMPERATURE,
                                        MachineNodeStatus.OFF,
                                        value.toInt()
                                    )

                                val offBurnerCommand =
                                    MachineStateInterpreter.generateControlCommand(
                                        machineState.value,
                                        MachineControlUnitIds.GAS_LEVEL,
                                        MachineNodeStatus.OFF,
                                        0
                                    )
                                enqueueCommand(offPreheatCommand)
                                enqueueCommand(offBurnerCommand)
                            }
                        }

                        "Gas Level" -> {
                            coroutineScope.launch {
                                val command = MachineStateInterpreter.generateControlCommand(
                                    machineState.value,
                                    MachineControlUnitIds.GAS_LEVEL,
                                    MachineNodeStatus.OFF,
                                    value.toInt()
                                )
                                enqueueCommand(command)
                            }
                        }
                    }
                    showDialog.value = false
                }
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
                    .clip(MaterialTheme.shapes.medium), contentAlignment = Alignment.TopCenter
            ) {
                RoastingGraph(machineState, roastingGraphViewModel)
                Text(
                    text =
                    if (isStartRoasting.value) roastingGraphViewModel.getFormattedTime() else "",
                    style = MaterialTheme.typography.displayMedium
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                item {
                    RoastingTemperatureWidget(
                        "Bean",
                        machineState.value.beanTemperature,
                        machineState.value.beanTemperatureRor,
                        backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                        textColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }

                item {
                    RoastingTemperatureWidget(
                        "Drum",
                        machineState.value.drumTemperature,
                        machineState.value.drumTemperatureRor,
                        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                item {
                    RoastingTemperatureWidget(
                        "Inlet",
                        machineState.value.airInletTemperature,
                        machineState.value.airInletTemperatureRor,
                        backgroundColor = MaterialTheme.colorScheme.errorContainer,
                        textColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                }

                item {
                    RoastingTemperatureWidget(
                        "Exhaust",
                        machineState.value.exhaustTemperature,
                        machineState.value.exhaustTemperatureRor,
                        backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
                        textColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }


            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Event", style = MaterialTheme.typography.headlineSmall)

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        item {
                            SwitchWithLabel(
                                label = "Yellow",
                                state = false,
                            )
                        }

                        item {
                            SwitchWithLabel(
                                label = "First Crack",
                                state = false,
                            )
                        }

                        item {
                            SwitchWithLabel(
                                label = "Second Crack",
                                state = false,
                            )
                        }

                        item {
                            SwitchWithLabel(
                                label = if (isStartRoasting.value) "END ROAST" else "START ROAST",
                                state = isStartRoasting.value,
                                disabled = machineState.value.beanTemperature < machineState.value.preheatTemperature,
                                onStateChange = {
                                    if (!isStartRoasting.value) {
                                        isStartRoasting.value = true

                                        startRoasting(
                                            coroutineScope,
                                            machineState,
                                            enqueueCommand,
                                            roastingGraphViewModel,
                                        )
                                    } else {
                                        isStartRoasting.value = false

                                        endRoasting(
                                            coroutineScope,
                                            machineState,
                                            enqueueCommand,
                                            roastingGraphViewModel,
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Control", style = MaterialTheme.typography.headlineSmall)

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        item {
                            SwitchWithLabel(
                                label = "Preheat",
                                value = if (!machineState.value.preheatTemperatureOnStatus) "OFF" else "${machineState.value.preheatTemperature.toInt()} C",
                                state = machineState.value.preheatTemperatureOnStatus,
                                onStateChange = {
                                    currentField.value = "Preheat Temperature"
                                    currentValue.value =
                                        machineState.value.preheatTemperature.toInt().toString()
                                    showDialog.value = true
                                    showTurnOffButton.value = true
                                    minValue.intValue = 120
                                    maxValue.intValue = 300
                                },
                            )
                        }
                        item {
                            SwitchWithLabel(
                                label = "Gas",
                                value = "${if (machineState.value.gasOnStatus) machineState.value.gasLevel else "OFF"} (${
                                    String.format(
                                        "%6.3f",
                                        machineState.value.gasPressure
                                    )
                                } Pa)",
                                state = machineState.value.gasOnStatus,
                                onStateChange = {
                                    currentField.value = "Gas Level"
                                    currentValue.value = machineState.value.gasLevel.toString()
                                    showDialog.value = true
                                    showTurnOffButton.value = true
                                    minValue.intValue = 0
                                    maxValue.intValue = 100
                                })
                        }
                        item {
                            SwitchWithLabel(
                                label = "Air",
                                value = "${machineState.value.fanLevel} (${machineState.value.airPressure} Pa)",
                                state = machineState.value.fanOnStatus,
                                onStateChange = {
                                    currentField.value = "Air Speed"
                                    currentValue.value = machineState.value.fanLevel.toString()
                                    showDialog.value = true
                                    showTurnOffButton.value = false
                                    minValue.intValue = 20
                                    maxValue.intValue = 100
                                })

                        }
                        item {
                            SwitchWithLabel(
                                label = "Drum RPM",
                                value = "${machineState.value.drumRpm}",
                                state = machineState.value.drumOnStatus,
                                onStateChange = {
                                    currentField.value = "Drum RPM"
                                    currentValue.value = machineState.value.drumRpm.toString()
                                    showDialog.value = true
                                    showTurnOffButton.value = false
                                    minValue.intValue = 10
                                    maxValue.intValue = 79
                                })
                        }
                        item {
                            SwitchWithLabel(
                                label = "Drum Door",
                                value = if (machineState.value.drumDoorOpenStatus) "OPEN" else "CLOSE",
                                state = machineState.value.drumDoorOpenStatus,
                                onStateChange = {
                                    coroutineScope.launch {
                                        val command =
                                            MachineStateInterpreter.generateControlCommand(
                                                machineState.value,
                                                MachineControlUnitIds.DRUM_DOOR,
                                                if (it) MachineNodeStatus.ON else MachineNodeStatus.OFF,
                                                0
                                            )
                                        enqueueCommand(command)
                                    }
                                })
                        }
                        item {
                            SwitchWithLabel(
                                label = "Bean Holder",
                                value = if (machineState.value.beanHolderOpenStatus) "OPEN" else "CLOSE",
                                state = machineState.value.beanHolderOpenStatus,
                                onStateChange = {
                                    coroutineScope.launch {
                                        val command =
                                            MachineStateInterpreter.generateControlCommand(
                                                machineState.value,
                                                MachineControlUnitIds.BEAN_HOLDER,
                                                if (it) MachineNodeStatus.ON else MachineNodeStatus.OFF,
                                                0
                                            )
                                        enqueueCommand(command)
                                    }
                                })
                        }
                        item {
                            SwitchWithLabel(
                                label = "Cooling Fan",
                                value = if (machineState.value.coolingTrayFanRunningStatus) "ON" else "OFF",
                                state = machineState.value.coolingTrayFanRunningStatus,
                                onStateChange = {
                                    coroutineScope.launch {
                                        val command =
                                            MachineStateInterpreter.generateControlCommand(
                                                machineState.value,
                                                MachineControlUnitIds.COOLING_TRAY_FAN,
                                                if (it) MachineNodeStatus.ON else MachineNodeStatus.OFF,
                                                0
                                            )
                                        enqueueCommand(command)
                                    }
                                })
                        }
                        item {
                            SwitchWithLabel(
                                label = "Cooling Stir",
                                value = if (machineState.value.coolingTrayStirRunningStatus) "ON" else "OFF",
                                state = machineState.value.coolingTrayStirRunningStatus,
                                onStateChange = {
                                    coroutineScope.launch {
                                        val command =
                                            MachineStateInterpreter.generateControlCommand(
                                                machineState.value,
                                                MachineControlUnitIds.COOLING_TRAY_STIR,
                                                if (it) MachineNodeStatus.ON else MachineNodeStatus.OFF,
                                                0
                                            )
                                        enqueueCommand(command)
                                    }
                                })
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    name = "Roasting light theme",
    widthDp = 1200,
    heightDp = 1920,
    showBackground = true,
    apiLevel = 34,
    uiMode = UI_MODE_NIGHT_NO
)
@Preview(
    name = "Roasting dark theme",
    widthDp = 1200,
    heightDp = 1920,
    showBackground = true,
    apiLevel = 34,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun RoastingContentPreview() {
    HBPeakTheme {
        RoastingContent()
    }
}