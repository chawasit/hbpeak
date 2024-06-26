package coffee.hh.hbpeak

import java.time.Instant

data class MachineState(
    val preheatTemperatureOnStatus: Boolean = false,
    val preheatTemperature: Float = 0.0f,
    val gasOnStatus: Boolean = false,
    val gasLevel: Int = 0,
    val drumRpm: Int = 0,
    val drumOnStatus: Boolean = false,
    val fanLevel: Int = 0,
    val fanOnStatus: Boolean = false,
    val coolingTrayFanRunningStatus: Boolean = false,
    val coolingTrayStirRunningStatus: Boolean = false,
    val beanHolderOpenStatus: Boolean = false,
    val drumDoorOpenStatus: Boolean = false,
    val dropDoorOpenStatus: Boolean = false,
    val loaderRunningStatus: Boolean = false,
    val destonerRunningStatus: Boolean = false,
    val exhaustFilterRunningStatus: Boolean = false,
    val beanTemperatureStatus: Boolean = false,
    val beanTemperature: Float = 100.0f,
    val exhaustTemperatureStatus: Boolean = false,
    val exhaustTemperature: Float = 120.0f,
    val airInletTemperatureStatus: Boolean = false,
    val airInletTemperature: Float = 200.0f,
    val drumTemperatureStatus: Boolean = false,
    val drumTemperature: Float = 180.0f,
    val airPressureStatus: Boolean = false,
    val airPressure: Float = 0.0f,
    val gasPressureStatus: Boolean = false,
    val gasPressure: Float = 0.0f,
    val beanTemperatureRor: Float = 3.0f,
    val exhaustTemperatureRor: Float = 6.0f,
    val airInletTemperatureRor: Float = 10.0f,
    val drumTemperatureRor: Float = 15.0f,
    val powerOnStatus: Boolean = false,
    val hmiRelayStatus: Boolean = false,
    val hasNewError: Boolean = false,
    val errors: List<MachineError> = emptyList(),
    val roastingStatus: Boolean = false,
    val startRoastingTime: Long = 0
)

data class MachineError(
    val time: Instant,
    val message: String
)
