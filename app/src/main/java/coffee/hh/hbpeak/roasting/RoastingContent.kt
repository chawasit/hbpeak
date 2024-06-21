package coffee.hh.hbpeak.roasting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coffee.hh.hbpeak.MachineControlUnitIds
import coffee.hh.hbpeak.MachineState
import coffee.hh.hbpeak.MachineStateInterpreter
import coffee.hh.hbpeak.MachineStatus
import coffee.hh.hbpeak.composable.NumberPadDialog
import coffee.hh.hbpeak.composable.SwitchWithLabel
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberEndAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineSpec
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.of
import com.patrykandpatrick.vico.compose.common.shader.color
import com.patrykandpatrick.vico.compose.common.shader.verticalGradient
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.AxisValueOverrider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModel
import com.patrykandpatrick.vico.core.cartesian.data.LineCartesianLayerModel
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.shader.DynamicShader
import kotlinx.coroutines.launch

@Preview(widthDp = 1000, heightDp = 1480, showBackground = true, apiLevel = 34)
@Composable
fun RoastingContent(
    machineState: MutableState<MachineState> = mutableStateOf(MachineState()),
    enqueueCommand: (String) -> Unit = fun(_) {},
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
                                    machineState.value,
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
                                    machineState.value,
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
                                    machineState.value,
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
                                    machineState.value,
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
                                    machineState.value,
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
                                    machineState.value,
                                    MachineControlUnitIds.FAN_LEVEL,
                                    MachineStatus.OFF,
                                    0
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
                                        MachineStatus.OFF,
                                        0
                                    )

                                val offBurnerCommand =
                                    MachineStateInterpreter.generateControlCommand(
                                        machineState.value,
                                        MachineControlUnitIds.GAS_LEVEL,
                                        MachineStatus.OFF,
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

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(2f)
                    .background(Color.Red)
            ) {
                RoastingGraph(machineState)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .background(Color.Yellow)
            ) {
                Text("Info Box")
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .background(Color.Green)
            ) {
                Text("Event Box")
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    item {
                        SwitchWithLabel(
                            label = "Preheat: ${machineState.value.preheatTemperature}",
                            state = machineState.value.preheatTemperatureOnStatus,
                            onStateChange = {
                                currentField.value = "Preheat Temperature"
                                currentValue.value =
                                    machineState.value.preheatTemperature.toInt().toString()
                                showDialog.value = true
                                showTurnOffButton.value = true
                                minValue.intValue = 0
                                maxValue.intValue = 300
                            },
                        )
                    }
                    item {
                        SwitchWithLabel(
                            label = "Gas Level: ${machineState.value.gasLevel}",
                            state = machineState.value.gasOnStatus,
                            onStateChange = {
                                currentField.value = "Gas Level"
                                currentValue.value = machineState.value.gasLevel.toInt().toString()
                                showDialog.value = true
                                showTurnOffButton.value = true
                                minValue.intValue = 0
                                maxValue.intValue = 99
                            })
                    }
                    item {
                        SwitchWithLabel(
                            label = "Air: ${machineState.value.fanLevel}",
                            state = machineState.value.fanOnStatus,
                            onStateChange = {
                                currentField.value = "Air Speed"
                                currentValue.value = machineState.value.fanLevel.toString()
                                showDialog.value = true
                                showTurnOffButton.value = false
                                minValue.intValue = 30
                                maxValue.intValue = 100
                            })

                    }
                    item {
                        SwitchWithLabel(
                            label = "Drum RPM: ${machineState.value.drumRpm}",
                            state = machineState.value.drumOnStatus,
                            onStateChange = {
                                currentField.value = "Drum RPM"
                                currentValue.value = machineState.value.drumRpm.toString()
                                showDialog.value = true
                                showTurnOffButton.value = false
                                minValue.intValue = 20
                                maxValue.intValue = 79
                            })
                    }
                    item {
                        SwitchWithLabel(
                            label = "Drum Door",
                            state = machineState.value.drumDoorOpenStatus,
                            onStateChange = {
                                coroutineScope.launch {
                                    val command = MachineStateInterpreter.generateControlCommand(
                                        machineState.value,
                                        MachineControlUnitIds.DRUM_DOOR,
                                        if (it) MachineStatus.ON else MachineStatus.OFF,
                                        0
                                    )
                                    enqueueCommand(command)
                                }
                            })
                    }
                    item {
                        SwitchWithLabel(
                            label = "Bean Holder",
                            state = machineState.value.beanHolderOpenStatus,
                            onStateChange = {
                                coroutineScope.launch {
                                    val command = MachineStateInterpreter.generateControlCommand(
                                        machineState.value,
                                        MachineControlUnitIds.BEAN_HOLDER,
                                        if (it) MachineStatus.ON else MachineStatus.OFF,
                                        0
                                    )
                                    enqueueCommand(command)
                                }
                            })
                    }
                    item {
                        SwitchWithLabel(
                            label = "Cooling Tray Fan",
                            state = machineState.value.coolingTrayFanRunningStatus,
                            onStateChange = {
                                coroutineScope.launch {
                                    val command = MachineStateInterpreter.generateControlCommand(
                                        machineState.value,
                                        MachineControlUnitIds.COOLING_TRAY_FAN,
                                        if (it) MachineStatus.ON else MachineStatus.OFF,
                                        0
                                    )
                                    enqueueCommand(command)
                                }
                            })
                    }
                    item {
                        SwitchWithLabel(
                            label = "Cooling Tray Stir",
                            state = machineState.value.coolingTrayStirRunningStatus,
                            onStateChange = {
                                coroutineScope.launch {
                                    val command = MachineStateInterpreter.generateControlCommand(
                                        machineState.value,
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
        }
    }
}

@Composable
fun RoastingGraph(machineState: MutableState<MachineState>) {
    val scrollState = rememberVicoScrollState()
    val zoomState = rememberVicoZoomState()
    LineChartDark()
}


private val model3 =
    CartesianChartModel(
        LineCartesianLayerModel.build {
            series(1, 2, 3, 4, 5)
            series(1, 3, 1, 2, 3)
        }
    )

@Preview("Line Chart Dark", widthDp = 200)
@Composable
fun LineChartDark() {
    Surface(shape = RoundedCornerShape(8.dp), color = Color.Gray) {
        val blue = Color.Cyan
        val red = Color.Red

        val label =
            rememberAxisLabelComponent(
                textSize = MaterialTheme.typography.bodyMedium.fontSize,
                padding = Dimensions.of(horizontal = 2.dp, vertical = 8.dp),
                margins = Dimensions.of(horizontal = 4.dp, vertical = 4.dp),
            )

        CartesianChartHost(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            chart =
            rememberCartesianChart(
                rememberLineCartesianLayer(
                    listOf(
                        rememberLineSpec(
                            thickness = 4.dp,
                            shader = DynamicShader.color(blue),
                            backgroundShader =
                            DynamicShader.verticalGradient(
                                arrayOf(blue.copy(alpha = 0.5f), blue.copy(alpha = 0f))
                            ),
                        ),
                        rememberLineSpec(
                            thickness = 2.dp,
                            cap = StrokeCap.Round,
                            shader = DynamicShader.color(red),
                            backgroundShader = null
                        ),
                    ),
                    axisValueOverrider = AxisValueOverrider.fixed(maxY = 4f),
                ),
                startAxis =
                rememberStartAxis(
                    horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Inside,
                    label = label,
                ),
                endAxis =
                rememberEndAxis(
                    horizontalLabelPosition = VerticalAxis.HorizontalLabelPosition.Inside,
                    guideline = null,
                    label = label,
                ),
            ),
            model = model3,
        )
    }
}

