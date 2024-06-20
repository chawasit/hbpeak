@file:Suppress("PropertyName")

package coffee.hh.hbpeak

import kotlinx.serialization.Serializable

@Serializable
data class CommandMessage(
    val command: String,
    val id: Int,
    val params: Map<String, Int>? = null,
    val event: String? = null,
    val roasterID: Int? = 0
)

@Serializable
data class DataResponse(
    val id: Int,
    val data: Data
)

@Serializable
data class Data(
    val BEAN_TEMPERATURE: Double,
    val EXHAUST_TEMPERATURE: Double,
    val AIR_INLET_TEMP: Double,
    val DRUM_TEMP: Double,
    val BEAN_TEMP_ROR: Double,
    val EXHAUST_TEMP_ROR: Double,
    val AIR_INLET_TEMP_ROR: Double,
    val DRUM_TEMP_ROR: Double,
    val GAS_LEVEL: Int,
    val DRUM_RPM: Int,
    val FAN_LEVEL: Int,
    val COOLING_TRAY_FAN: Boolean,
    val COOLING_TRAY_STIR: Boolean,
    val BEAN_HOLDER: Boolean,
    val DRUM_DOOR: Boolean,
    val DROP_DOOR: Boolean,
    val LOADER: Boolean,
    val DESTONER: Boolean,
    val EXHAUST_FILTER: Boolean,
    val AIR_PRESSURE: Double,
    val GAS_PRESSURE: Double
)

@Serializable
data class EventResponse(
    val id: Int,
    val data: Map<String, String>
)

@Serializable
data class ControlCommand(
    val t: Int,
    val s: String,
    val v: Int
)

@Serializable
data class ControlCommands(
    val m: String,
    val c: Int,
    val l: List<ControlCommand>
)