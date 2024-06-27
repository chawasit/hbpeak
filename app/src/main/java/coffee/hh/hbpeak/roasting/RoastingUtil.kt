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
    enqueueCommand: (String) -> Unit,
) {
    coroutineScope.launch {
        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                MachineControlUnitIds.DRUM_DOOR,
                MachineNodeStatus.OFF,
            )
        )

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                MachineControlUnitIds.ROASTING_STATUS,
                MachineNodeStatus.ON,
            )
        )

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                MachineControlUnitIds.BEAN_HOLDER,
                MachineNodeStatus.ON
            )
        )

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                MachineControlUnitIds.PREHEAT_TEMPERATURE,
                MachineNodeStatus.OFF
            )
        )

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                MachineControlUnitIds.GAS_LEVEL,
                MachineNodeStatus.ON,
                20
            )
        )

        delay(15 * 1000)

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                MachineControlUnitIds.BEAN_HOLDER,
                MachineNodeStatus.OFF
            )
        )
    }
}

fun endRoasting(
    coroutineScope: CoroutineScope,
    enqueueCommand: (String) -> Unit,
) {
    coroutineScope.launch {
        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                MachineControlUnitIds.ROASTING_STATUS,
                MachineNodeStatus.OFF,
            )
        )

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                MachineControlUnitIds.GAS_LEVEL,
                MachineNodeStatus.OFF,
            )
        )

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                MachineControlUnitIds.PREHEAT_TEMPERATURE,
                MachineNodeStatus.OFF,
            )
        )

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                MachineControlUnitIds.FAN_LEVEL,
                MachineNodeStatus.ON,
                60
            )
        )

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                MachineControlUnitIds.COOLING_TRAY_FAN,
                MachineNodeStatus.ON
            )
        )

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                MachineControlUnitIds.COOLING_TRAY_STIR,
                MachineNodeStatus.ON
            )
        )

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                MachineControlUnitIds.DRUM_DOOR,
                MachineNodeStatus.ON
            )
        )

        // Repeat Off Status
        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                MachineControlUnitIds.ROASTING_STATUS,
                MachineNodeStatus.OFF,
            )
        )

        delay(30 * 1000)

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                MachineControlUnitIds.DRUM_DOOR,
                MachineNodeStatus.OFF
            )
        )

        delay(120 * 1000)

        enqueueCommand(
            MachineStateInterpreter.generateControlCommand(
                MachineControlUnitIds.COOLING_TRAY_FAN,
                MachineNodeStatus.OFF
            )
        )
    }
}