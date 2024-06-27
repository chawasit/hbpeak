package coffee.hh.hbpeak.roasting

import androidx.compose.runtime.MutableState
import coffee.hh.hbpeak.MachineControlUnitIds
import coffee.hh.hbpeak.MachineNodeStatus
import coffee.hh.hbpeak.MachineState
import coffee.hh.hbpeak.MachineStateInterpreter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun startRoasting(
    coroutineScope: CoroutineScope,
    machineState: MutableState<MachineState>,
    enqueueCommand: (String) -> Unit,
    roastingGraphViewModel: RoastingGraphViewModel,
) {
    coroutineScope.launch {

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                machineState.value,
                MachineControlUnitIds.DRUM_DOOR,
                MachineNodeStatus.OFF,
            )
        )

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                machineState.value,
                MachineControlUnitIds.ROASTING_STATUS,
                MachineNodeStatus.ON,
            )
        )

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                machineState.value,
                MachineControlUnitIds.BEAN_HOLDER,
                MachineNodeStatus.ON
            )
        )

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                machineState.value,
                MachineControlUnitIds.PREHEAT_TEMPERATURE,
                MachineNodeStatus.OFF
            )
        )

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                machineState.value,
                MachineControlUnitIds.GAS_LEVEL,
                MachineNodeStatus.ON,
                20
            )
        )

        roastingGraphViewModel.startRoasting()

        delay(15 * 1000)

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                machineState.value,
                MachineControlUnitIds.BEAN_HOLDER,
                MachineNodeStatus.OFF
            )
        )
    }
}

fun endRoasting(
    coroutineScope: CoroutineScope,
    machineState: MutableState<MachineState>,
    enqueueCommand: (String) -> Unit,
    roastingGraphViewModel: RoastingGraphViewModel,
) {
    coroutineScope.launch {
        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                machineState.value,
                MachineControlUnitIds.ROASTING_STATUS,
                MachineNodeStatus.OFF,
            )
        )

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                machineState.value,
                MachineControlUnitIds.GAS_LEVEL,
                MachineNodeStatus.OFF,
            )
        )

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                machineState.value,
                MachineControlUnitIds.PREHEAT_TEMPERATURE,
                MachineNodeStatus.OFF,
            )
        )

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                machineState.value,
                MachineControlUnitIds.FAN_LEVEL,
                MachineNodeStatus.ON,
                60
            )
        )

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                machineState.value,
                MachineControlUnitIds.COOLING_TRAY_FAN,
                MachineNodeStatus.ON
            )
        )

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                machineState.value,
                MachineControlUnitIds.COOLING_TRAY_STIR,
                MachineNodeStatus.ON
            )
        )

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                machineState.value,
                MachineControlUnitIds.DRUM_DOOR,
                MachineNodeStatus.ON
            )
        )

        // Repeat Off Status
        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                machineState.value,
                MachineControlUnitIds.ROASTING_STATUS,
                MachineNodeStatus.OFF,
            )
        )

        delay(30 * 1000)

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                machineState.value,
                MachineControlUnitIds.DRUM_DOOR,
                MachineNodeStatus.OFF
            )
        )

        delay(120 * 1000)

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                machineState.value,
                MachineControlUnitIds.COOLING_TRAY_FAN,
                MachineNodeStatus.OFF
            )
        )
    }
}