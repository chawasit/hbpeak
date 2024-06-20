package coffee.hh.hbpeak

object MachineControlUnitIds {
    const val PREHEAT_TEMPERATURE = 226
    const val GAS_LEVEL = 1
    const val DRUM_RPM = 2
    const val FAN_LEVEL = 3
    const val COOLING_TRAY_FAN = 4
    const val COOLING_TRAY_STIR = 5
    const val BEAN_HOLDER = 6
    const val DRUM_DOOR = 7
    const val DROP_DOOR = 8
    const val LOADER = 9
    const val DESTONER = 10
    const val EXHAUST_FILTER = 11
    const val BEAN_TEMPERATURE = 16
    const val EXHAUST_TEMPERATURE = 17
    const val AIR_INLET_TEMPERATURE = 18
    const val DRUM_TEMPERATURE = 19
    const val AIR_PRESSURE = 32
    const val GAS_PRESSURE = 33
    const val POWER_ON = 229
    const val HMI_RELAY = 227
}

object MachineStatus {
    const val OFF = "f"
    const val ON = "o"
}

object MachineMessageTypes {
    const val CONTROL = "c"
    const val GET = "g"
    const val ERROR = "e"
    const val RESPONSE = "r"
}

val websocketNameMapping = mapOf(
    "preheat_temperature" to MachineControlUnitIds.PREHEAT_TEMPERATURE,
    "gas_level" to MachineControlUnitIds.GAS_LEVEL,
    "drum_rpm" to MachineControlUnitIds.DRUM_RPM,
    "fan_level" to MachineControlUnitIds.FAN_LEVEL,
    "cooling_tray_fan" to MachineControlUnitIds.COOLING_TRAY_FAN,
    "cooling_tray_stir" to MachineControlUnitIds.COOLING_TRAY_STIR,
    "bean_holder" to MachineControlUnitIds.BEAN_HOLDER,
    "drum_door" to MachineControlUnitIds.DRUM_DOOR,
    "drop_door" to MachineControlUnitIds.DROP_DOOR,
    "loader" to MachineControlUnitIds.LOADER,
    "destoner" to MachineControlUnitIds.DESTONER,
    "exhaust_filter" to MachineControlUnitIds.EXHAUST_FILTER,
    "bean_temperature" to MachineControlUnitIds.BEAN_TEMPERATURE,
    "exhaust_temperature" to MachineControlUnitIds.EXHAUST_TEMPERATURE,
    "air_inlet_temperature" to MachineControlUnitIds.AIR_INLET_TEMPERATURE,
    "drum_temperature" to MachineControlUnitIds.DRUM_TEMPERATURE,
    "air_pressure" to MachineControlUnitIds.AIR_PRESSURE,
    "gas_pressure" to MachineControlUnitIds.GAS_PRESSURE,
    "power_on" to MachineControlUnitIds.POWER_ON,
    "hmi_relay" to MachineControlUnitIds.HMI_RELAY
)