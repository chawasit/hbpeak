package coffee.hh.hbpeak

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import org.json.JSONObject
import java.time.LocalDateTime

object MachineStateInterpreter {

    @RequiresApi(Build.VERSION_CODES.O)
    fun interpretMessage(currentState: MachineState, message: String): MachineState {
        try {
            val json = JSONObject(message)

            val method = json.getString("m")
            val params = json.getJSONArray("l")

            var newState = currentState.copy()
            when (method) {
                MachineMessageTypes.RESPONSE -> {
                    for (i in 0 until params.length()) {
                        val param = params.getJSONObject(i)
                        val target = param.getInt("t")
                        val status = param.optString("s", MachineNodeStatus.OFF)
                        val value = param.optDouble("v", Double.NaN)
                        val rateOfRise = param.optDouble("r", Double.NaN)

                        newState = when (target) {
                            MachineControlUnitIds.PREHEAT_TEMPERATURE -> newState.copy(
                                preheatTemperature = value.toFloat(),
                                preheatTemperatureOnStatus = machineStatusToBoolean(status)
                            )

                            MachineControlUnitIds.GAS_LEVEL -> newState.copy(
                                gasLevel = value.toInt() / 10,
                                gasOnStatus = machineStatusToBoolean(status)
                            )

                            MachineControlUnitIds.DRUM_RPM -> newState.copy(
                                drumRpm = value.toInt(),
                                drumOnStatus = machineStatusToBoolean(status)
                            )

                            MachineControlUnitIds.FAN_LEVEL -> newState.copy(
                                fanLevel = value.toInt(),
                                fanOnStatus = machineStatusToBoolean(status)
                            )

                            MachineControlUnitIds.COOLING_TRAY_FAN -> newState.copy(
                                coolingTrayFanRunningStatus = machineStatusToBoolean(status),
                            )

                            MachineControlUnitIds.COOLING_TRAY_STIR -> newState.copy(
                                coolingTrayStirRunningStatus = machineStatusToBoolean(status)
                            )

                            MachineControlUnitIds.BEAN_HOLDER -> newState.copy(
                                beanHolderOpenStatus = machineStatusToBoolean(status)
                            )

                            MachineControlUnitIds.DRUM_DOOR -> newState.copy(
                                drumDoorOpenStatus = machineStatusToBoolean(status)
                            )

                            MachineControlUnitIds.DROP_DOOR -> newState.copy(
                                dropDoorOpenStatus = machineStatusToBoolean(status)
                            )

                            MachineControlUnitIds.LOADER -> newState.copy(
                                loaderRunningStatus = machineStatusToBoolean(status)
                            )

                            MachineControlUnitIds.DESTONER -> newState.copy(
                                destonerRunningStatus = machineStatusToBoolean(status)
                            )

                            MachineControlUnitIds.EXHAUST_FILTER -> newState.copy(
                                exhaustFilterRunningStatus = machineStatusToBoolean(status)
                            )

                            MachineControlUnitIds.BEAN_TEMPERATURE -> newState.copy(
                                beanTemperature = value.toFloat() / 10f,
                                beanTemperatureRor = rateOfRise.toFloat() / 10f,
                                beanTemperatureStatus = machineStatusToBoolean(status)
                            )

                            MachineControlUnitIds.EXHAUST_TEMPERATURE -> newState.copy(
                                exhaustTemperature = value.toFloat() / 10f,
                                exhaustTemperatureRor = rateOfRise.toFloat() / 10f,
                                exhaustTemperatureStatus = machineStatusToBoolean(status)
                            )

                            MachineControlUnitIds.AIR_INLET_TEMPERATURE -> newState.copy(
                                airInletTemperature = value.toFloat() / 10f,
                                airInletTemperatureRor = rateOfRise.toFloat() / 10f,
                                airInletTemperatureStatus = machineStatusToBoolean(status)
                            )

                            MachineControlUnitIds.DRUM_TEMPERATURE -> newState.copy(
                                drumTemperature = value.toFloat() / 10f,
                                drumTemperatureRor = rateOfRise.toFloat() / 10f,
                                drumTemperatureStatus = machineStatusToBoolean(status)
                            )

                            MachineControlUnitIds.AIR_PRESSURE -> newState.copy(
                                airPressure = value.toFloat(),
                                airPressureStatus = machineStatusToBoolean(status)
                            )

                            MachineControlUnitIds.GAS_PRESSURE -> newState.copy(
                                gasPressure = value.toFloat() / 1000f,
                                gasPressureStatus = machineStatusToBoolean(status)
                            )

                            MachineControlUnitIds.POWER_ON -> newState.copy(
                                powerOnStatus = machineStatusToBoolean(status)
                            )

                            MachineControlUnitIds.HMI_RELAY -> newState.copy(
                                hmiRelayStatus = machineStatusToBoolean(status)
                            )

                            MachineControlUnitIds.ROASTING_STATUS -> {
                                val roastStatus = machineStatusToBoolean(status)

                                if (roastStatus == currentState.roastingStatus) {
                                    newState
                                } else {
                                    newState.copy(
                                        roastingStatus = roastStatus,
                                        startRoastingTime = System.currentTimeMillis()
                                    )
                                }
                            }

                            else -> {
                                Log.e(
                                    "MachineStateInterpreter",
                                    "Unknown target: $target with params: $params"
                                )
                                newState
                            }
                        }
                    }
                }

                MachineMessageTypes.ERROR -> {
                    for (i in 0 until params.length()) {
                        val param = params.getJSONObject(i)
                        val target = param.getInt("t")
                        val status = param.optString("s", "f")

                        newState = when (target) {
                            MachineControlUnitIds.PREHEAT_TEMPERATURE -> newState.copy(
                                preheatTemperatureOnStatus = machineStatusToBoolean(status),
                                hasNewError = true,
                                errors = newState.errors + MachineError(
                                    LocalDateTime.now(), "Preheat Failed"
                                )
                            )

                            MachineControlUnitIds.GAS_LEVEL -> newState.copy(
                                gasOnStatus = machineStatusToBoolean(status),
                                hasNewError = true,
                                errors = newState.errors + MachineError(
                                    LocalDateTime.now(), "Burner Ignition Failed"
                                )
                            )

                            MachineControlUnitIds.DRUM_RPM -> newState.copy(
                                drumOnStatus = machineStatusToBoolean(status),
                                hasNewError = true,
                                errors = newState.errors + MachineError(
                                    LocalDateTime.now(), "Drum Motor Failed"
                                )
                            )

                            MachineControlUnitIds.FAN_LEVEL -> newState.copy(
                                fanOnStatus = machineStatusToBoolean(status),
                                hasNewError = true,
                                errors = newState.errors + MachineError(
                                    LocalDateTime.now(), "Fan Motor Failed"
                                )
                            )

                            MachineControlUnitIds.COOLING_TRAY_FAN -> newState.copy(
                                coolingTrayFanRunningStatus = machineStatusToBoolean(status),
                                hasNewError = true,
                                errors = newState.errors + MachineError(
                                    LocalDateTime.now(), "Cooling Tray Fan Failed"
                                )
                            )

                            MachineControlUnitIds.COOLING_TRAY_STIR -> newState.copy(
                                coolingTrayStirRunningStatus = machineStatusToBoolean(status),
                                hasNewError = true,
                                errors = newState.errors + MachineError(
                                    LocalDateTime.now(), "Cooling Tray Stir Motor Failed"
                                )
                            )

                            MachineControlUnitIds.BEAN_HOLDER -> newState.copy(
                                beanHolderOpenStatus = machineStatusToBoolean(status),
                                hasNewError = true,
                                errors = newState.errors + MachineError(
                                    LocalDateTime.now(), "Bean Holder Failed"
                                )
                            )

                            MachineControlUnitIds.DRUM_DOOR -> newState.copy(
                                drumDoorOpenStatus = machineStatusToBoolean(status),
                                hasNewError = true,
                                errors = newState.errors + MachineError(
                                    LocalDateTime.now(), "Drum Door Failed"
                                )
                            )

                            MachineControlUnitIds.DROP_DOOR -> newState.copy(
                                dropDoorOpenStatus = machineStatusToBoolean(status),
                                hasNewError = true,
                                errors = newState.errors + MachineError(
                                    LocalDateTime.now(), "Drop Door Failed"
                                )
                            )

                            MachineControlUnitIds.LOADER -> newState.copy(
                                loaderRunningStatus = machineStatusToBoolean(status),
                                hasNewError = true,
                                errors = newState.errors + MachineError(
                                    LocalDateTime.now(), "Loader Failed"
                                )
                            )

                            MachineControlUnitIds.DESTONER -> newState.copy(
                                destonerRunningStatus = machineStatusToBoolean(status),
                                hasNewError = true,
                                errors = newState.errors + MachineError(
                                    LocalDateTime.now(), "Destoner Failed"
                                )
                            )

                            MachineControlUnitIds.EXHAUST_FILTER -> newState.copy(
                                exhaustFilterRunningStatus = machineStatusToBoolean(status),
                                hasNewError = true,
                                errors = newState.errors + MachineError(
                                    LocalDateTime.now(), "Exhaust Filter Failed"
                                )
                            )

                            MachineControlUnitIds.BEAN_TEMPERATURE -> newState.copy(
                                beanTemperatureStatus = machineStatusToBoolean(status),
                                hasNewError = true,
                                errors = newState.errors + MachineError(
                                    LocalDateTime.now(), "Bean Temperature Sensor Failed"
                                )
                            )

                            MachineControlUnitIds.EXHAUST_TEMPERATURE -> newState.copy(
                                exhaustTemperatureStatus = machineStatusToBoolean(status),
                                hasNewError = true,
                                errors = newState.errors + MachineError(
                                    LocalDateTime.now(), "Exhaust Temperature Sensor Failed"
                                )
                            )

                            MachineControlUnitIds.AIR_INLET_TEMPERATURE -> newState.copy(
                                airInletTemperatureStatus = machineStatusToBoolean(status),
                                hasNewError = true,
                                errors = newState.errors + MachineError(
                                    LocalDateTime.now(), "Air Inlet Temperature Sensor Failed"
                                )
                            )

                            MachineControlUnitIds.DRUM_TEMPERATURE -> newState.copy(
                                drumTemperatureStatus = machineStatusToBoolean(status),
                                hasNewError = true,
                                errors = newState.errors + MachineError(
                                    LocalDateTime.now(), "Drum Temperature Sensor Failed"
                                )
                            )

                            MachineControlUnitIds.AIR_PRESSURE -> newState.copy(
                                airPressureStatus = machineStatusToBoolean(status),
                                hasNewError = true,
                                errors = newState.errors + MachineError(
                                    LocalDateTime.now(), "Air Pressure Sensor Failed"
                                )
                            )

                            else -> {
                                Log.w(
                                    "MachineStateInterpreter",
                                    "Unknown Error target: $target with params: $params"
                                )
                                newState.copy(
                                    airPressureStatus = machineStatusToBoolean(status),
                                    hasNewError = true,
                                    errors = newState.errors + MachineError(
                                        LocalDateTime.now(),
                                        "Unknown Error! Check Logs for detail."
                                    )
                                )
                            }
                        }
                    }

                    Log.e("MachineStateInterpreter", "Got Error: ${newState.errors}")
                }
            }

            return newState
        } catch (e: Exception) {
            Log.e("MachineStateInterpreter", e.message ?: "Unknown error")
        }

        return currentState
    }

    fun generateControlCommand(
        target: Int,
        status: String? = null,
        value: Int? = 0
    ): String {
        val commandObject = mapOf("t" to target)
        if (status != null) commandObject.entries.plus("s" to status)
        if (value != null) commandObject.entries.plus("v" to value)

        val commandList = listOf(
            commandObject
        )
        val command = mapOf(
            "m" to MachineMessageTypes.CONTROL,
            "c" to commandList.size,
            "l" to commandList
        )

        return JSONObject(command).toString()
    }

    fun generateStatusRequestCommand(): String {
        val statusRequestTargets = listOf(
            MachineControlUnitIds.PREHEAT_TEMPERATURE,
            MachineControlUnitIds.GAS_LEVEL,
            MachineControlUnitIds.DRUM_RPM,
            MachineControlUnitIds.FAN_LEVEL,
            MachineControlUnitIds.COOLING_TRAY_FAN,
            MachineControlUnitIds.COOLING_TRAY_STIR,
            MachineControlUnitIds.BEAN_HOLDER,
            MachineControlUnitIds.DRUM_DOOR,
            MachineControlUnitIds.BEAN_TEMPERATURE,
            MachineControlUnitIds.EXHAUST_TEMPERATURE,
            MachineControlUnitIds.AIR_INLET_TEMPERATURE,
            MachineControlUnitIds.DRUM_TEMPERATURE,
            MachineControlUnitIds.AIR_PRESSURE,
            MachineControlUnitIds.GAS_PRESSURE,
            MachineControlUnitIds.ROASTING_STATUS
        )

        val commandList = statusRequestTargets.map { target ->
            mapOf("t" to target)
        }

        val command = mapOf(
            "m" to MachineMessageTypes.GET,
            "c" to commandList.size,
            "l" to commandList
        )

        return JSONObject(command).toString()
    }

    private fun machineStatusToBoolean(status: String): Boolean {
        return status == MachineNodeStatus.ON
    }
}