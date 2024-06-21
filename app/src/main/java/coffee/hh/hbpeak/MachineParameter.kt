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
    "PreheatTemperature" to MachineControlUnitIds.PREHEAT_TEMPERATURE,
    "GasLevel" to MachineControlUnitIds.GAS_LEVEL,
    "DrumRPM" to MachineControlUnitIds.DRUM_RPM,
    "FanLevel" to MachineControlUnitIds.FAN_LEVEL,
    "CoolingTrayFan" to MachineControlUnitIds.COOLING_TRAY_FAN,
    "CoolingTrayStir" to MachineControlUnitIds.COOLING_TRAY_STIR,
    "BeanHolder" to MachineControlUnitIds.BEAN_HOLDER,
    "DrumDoor" to MachineControlUnitIds.DRUM_DOOR,
    "DropDoor" to MachineControlUnitIds.DROP_DOOR,
    "Loader" to MachineControlUnitIds.LOADER,
    "Destoner" to MachineControlUnitIds.DESTONER,
    "ExhaustFilter" to MachineControlUnitIds.EXHAUST_FILTER,
    "PowerOn" to MachineControlUnitIds.POWER_ON,
    "HMIRelay" to MachineControlUnitIds.HMI_RELAY
)