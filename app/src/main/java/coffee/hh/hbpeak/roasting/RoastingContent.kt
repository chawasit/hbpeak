package coffee.hh.hbpeak.roasting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coffee.hh.hbpeak.MachineControlUnitIds
import coffee.hh.hbpeak.MachineState
import coffee.hh.hbpeak.MachineStateInterpreter
import coffee.hh.hbpeak.MachineStatus
import coffee.hh.hbpeak.composable.NumberPadDialog
import coffee.hh.hbpeak.composable.SwitchWithLabel
import kotlinx.coroutines.launch

@Composable
fun RoastingContent(
    contentPadding: PaddingValues,
    machineState: MachineState,
    enqueueCommand: (String) -> Unit,
) {
    val showDialog = remember { mutableStateOf(false) }
    val showTurnOffButton = remember { mutableStateOf(false) }
    val maxValue = remember { mutableIntStateOf(100) }
    val minValue = remember { mutableIntStateOf(0) }
    val currentField = remember { mutableStateOf("") }
    val currentValue = remember { mutableStateOf("") }
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
                                    machineState,
                                    MachineControlUnitIds.DRUM_RPM,
                                    if (value.toInt() == 0) MachineStatus.OFF else MachineStatus.ON,
                                    value.toInt()
                                )
                                enqueueCommand(command)
                            }
                        }

                        "Air Speed" -> {
                            coroutineScope.launch {
                                val command = MachineStateInterpreter.generateControlCommand(
                                    machineState,
                                    MachineControlUnitIds.FAN_LEVEL,
                                    if (value.toInt() == 0) MachineStatus.OFF else MachineStatus.ON,
                                    value.toInt()
                                )
                                enqueueCommand(command)
                            }
                        }

                        "Preheat Temperature" -> {
                            coroutineScope.launch {
                                val command = MachineStateInterpreter.generateControlCommand(
                                    machineState,
                                    MachineControlUnitIds.PREHEAT_TEMPERATURE,
                                    if (value.toInt() == 0) MachineStatus.OFF else MachineStatus.ON,
                                    value.toInt()
                                )
                                enqueueCommand(command)
                            }
                        }

                        "Gas Level" -> {
                            coroutineScope.launch {
                                val command = MachineStateInterpreter.generateControlCommand(
                                    machineState,
                                    MachineControlUnitIds.GAS_LEVEL,
                                    if (value.toInt() == 0) MachineStatus.OFF else MachineStatus.ON,
                                    value.toInt()
                                )
                                enqueueCommand(command)
                            }
                        }
                    }
                    showDialog.value = false
                },
                onTurnOff = {
                    when (currentField.value) {
                        "Drum RPM" -> {
                            coroutineScope.launch {
                                val command = MachineStateInterpreter.generateControlCommand(
                                    machineState,
                                    MachineControlUnitIds.DRUM_RPM,
                                    MachineStatus.OFF,
                                    0
                                )
                                enqueueCommand(command)
                            }
                        }

                        "Air Speed" -> {
                            coroutineScope.launch {
                                val command = MachineStateInterpreter.generateControlCommand(
                                    machineState,
                                    MachineControlUnitIds.FAN_LEVEL,
                                    MachineStatus.OFF,
                                    0
                                )
                                enqueueCommand(command)
                            }
                        }

                        "Preheat Temperature" -> {
                            coroutineScope.launch {
                                val command = MachineStateInterpreter.generateControlCommand(
                                    machineState,
                                    MachineControlUnitIds.PREHEAT_TEMPERATURE,
                                    MachineStatus.OFF,
                                    0
                                )
                                enqueueCommand(command)
                            }
                        }

                        "Gas Level" -> {
                            coroutineScope.launch {
                                val command = MachineStateInterpreter.generateControlCommand(
                                    machineState,
                                    MachineControlUnitIds.GAS_LEVEL,
                                    MachineStatus.OFF,
                                    0
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

    Box(modifier = Modifier.padding(contentPadding)) {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row {
                Text("Bean Temperature: ${machineState.beanTemperature} °C")
                Spacer(modifier = Modifier.width(8.dp))
                Text("RoR: ${machineState.beanTemperatureRor} °C/min")
            }
            Row {
                Text("Exhaust Temperature: ${machineState.exhaustTemperature} °C")
                Spacer(modifier = Modifier.width(8.dp))
                Text("RoR: ${machineState.exhaustTemperatureRor} °C/min")
            }
            Row {
                Text("Air Inlet Temperature: ${machineState.airInletTemperature} °C")
                Spacer(modifier = Modifier.width(8.dp))
                Text("RoR: ${machineState.airInletTemperatureRor} °C/min")
            }
            Row {
                Text("Drum Temperature: ${machineState.drumTemperature} °C")
                Spacer(modifier = Modifier.width(8.dp))
                Text("RoR: ${machineState.drumTemperatureRor} °C/min")
            }
            Text("Air Pressure: ${machineState.airPressure} Pa")
            Text("Gas Pressure: ${machineState.gasPressure} Pa")


            SwitchWithLabel(
                label = "Drum RPM: ${machineState.drumRpm}",
                state = machineState.drumOnStatus,
                onStateChange = {
                    currentField.value = "Drum RPM"
                    currentValue.value = machineState.drumRpm.toString()
                    showDialog.value = true
                    showTurnOffButton.value = false
                    minValue.intValue = 20
                    maxValue.intValue = 79
                })


            SwitchWithLabel(
                label = "Air: ${machineState.fanLevel}",
                state = machineState.fanOnStatus,
                onStateChange = {
                    currentField.value = "Air Speed"
                    currentValue.value = machineState.fanLevel.toString()
                    showDialog.value = true
                    showTurnOffButton.value = false
                    minValue.intValue = 30
                    maxValue.intValue = 100
                })


            SwitchWithLabel(
                label = "Preheat: ${machineState.preheatTemperature}",
                state = machineState.preheatTemperatureOnStatus,
                onStateChange = {
                    currentField.value = "Preheat Temperature"
                    currentValue.value = machineState.preheatTemperature.toInt().toString()
                    showDialog.value = true
                    showTurnOffButton.value = true
                    minValue.intValue = 0
                    maxValue.intValue = 300
                })

            SwitchWithLabel(
                label = "Gas Level: ${machineState.gasLevel}",
                state = machineState.gasOnStatus,
                onStateChange = {
                    currentField.value = "Gas Level"
                    currentValue.value = machineState.gasLevel.toInt().toString()
                    showDialog.value = true
                    showTurnOffButton.value = true
                    minValue.intValue = 0
                    maxValue.intValue = 99
                })

            SwitchWithLabel(
                label = "Drum Door",
                state = machineState.drumDoorOpenStatus,
                onStateChange = {
                    coroutineScope.launch {
                        val command = MachineStateInterpreter.generateControlCommand(
                            machineState,
                            MachineControlUnitIds.DRUM_DOOR,
                            if (it) MachineStatus.ON else MachineStatus.OFF,
                            0
                        )
                        enqueueCommand(command)
                    }
                })

            SwitchWithLabel(
                label = "Bean Holder",
                state = machineState.beanHolderOpenStatus,
                onStateChange = {
                    coroutineScope.launch {
                        val command = MachineStateInterpreter.generateControlCommand(
                            machineState,
                            MachineControlUnitIds.BEAN_HOLDER,
                            if (it) MachineStatus.ON else MachineStatus.OFF,
                            0
                        )
                        enqueueCommand(command)
                    }
                })

            SwitchWithLabel(
                label = "Cooling Tray Fan",
                state = machineState.coolingTrayFanRunningStatus,
                onStateChange = {
                    coroutineScope.launch {
                        val command = MachineStateInterpreter.generateControlCommand(
                            machineState,
                            MachineControlUnitIds.COOLING_TRAY_FAN,
                            if (it) MachineStatus.ON else MachineStatus.OFF,
                            0
                        )
                        enqueueCommand(command)
                    }
                })

            SwitchWithLabel(
                label = "Cooling Tray Stir",
                state = machineState.coolingTrayStirRunningStatus,
                onStateChange = {
                    coroutineScope.launch {
                        val command = MachineStateInterpreter.generateControlCommand(
                            machineState,
                            MachineControlUnitIds.COOLING_TRAY_STIR,
                            if (it) MachineStatus.ON else MachineStatus.OFF,
                            0
                        )
                        enqueueCommand(command)
                    }
                })
        }
    }
}