package coffee.hh.hbpeak


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSocketServerSession
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.DefaultWebSocketSession
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Collections

class WebSocketServer(
    private val machineState: MutableState<MachineState>,
    private val coroutineScope: CoroutineScope,
    private val sendMessageToMachine: (String) -> Unit
) {
    private val sessions: MutableList<WebSocketServerSession> =
        Collections.synchronizedList(ArrayList())

    @RequiresApi(Build.VERSION_CODES.O)
    fun create(): NettyApplicationEngine {
        return embeddedServer(Netty, port = 8765, module = { module() })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun Application.module() {
        install(WebSockets) {
            maxFrameSize = Long.MAX_VALUE
            masking = false
        }

        routing {
            webSocket("/ws") {
                sessions.add(this)
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val message = frame.readText()
                    handleWebSocketMessage(message, this)
                }

                sessions.remove(this)
            }
        }

    }

    private suspend fun handleWebSocketMessage(message: String, session: DefaultWebSocketSession) {
        try {
            val msg = Json.decodeFromString<CommandMessage>(message)
            when (msg.command) {
                "getData" -> sendDataResponse(session, msg.id)
                "setControlParams" -> handleRoasterControlParams(session, msg.params, msg.id)
                "setControlEvent" -> handleRoasterControlEvent(session, msg.event, msg.id)
                else -> println("Unknown Command: $msg")
            }
        } catch (e: Exception) {
            Log.e("WebSocketServer", "Error during handle websocket request $message {${e.message}}")
        }
    }

    private suspend fun sendDataResponse(session: DefaultWebSocketSession, messageId: Int) {
        val dataResponse = DataResponse(
            id = messageId,
            data = Data(
                BEAN_TEMPERATURE = machineState.value.beanTemperature.toDouble(),
                EXHAUST_TEMPERATURE = machineState.value.exhaustTemperature.toDouble(),
                AIR_INLET_TEMP = machineState.value.airInletTemperature.toDouble(),
                DRUM_TEMP = machineState.value.drumTemperature.toDouble(),
                BEAN_TEMP_ROR = machineState.value.beanTemperatureRor.toDouble(),
                EXHAUST_TEMP_ROR = machineState.value.exhaustTemperatureRor.toDouble(),
                AIR_INLET_TEMP_ROR = machineState.value.airInletTemperatureRor.toDouble(),
                DRUM_TEMP_ROR = machineState.value.drumTemperatureRor.toDouble(),
                GAS_LEVEL = machineState.value.gasLevel.toInt(),
                DRUM_RPM = machineState.value.drumRpm,
                FAN_LEVEL = machineState.value.fanLevel,
                COOLING_TRAY_FAN = machineState.value.coolingTrayFanRunningStatus,
                COOLING_TRAY_STIR = machineState.value.coolingTrayStirRunningStatus,
                BEAN_HOLDER = machineState.value.beanHolderOpenStatus,
                DRUM_DOOR = machineState.value.drumDoorOpenStatus,
                DROP_DOOR = machineState.value.dropDoorOpenStatus,
                LOADER = machineState.value.loaderRunningStatus,
                DESTONER = machineState.value.destonerRunningStatus,
                EXHAUST_FILTER = machineState.value.exhaustFilterRunningStatus,
                AIR_PRESSURE = machineState.value.airPressure.toDouble(),
                GAS_PRESSURE = machineState.value.gasPressure.toDouble()
            )
        )
        session.send(Frame.Text(Json.encodeToString(dataResponse)))
    }

    private suspend fun handleRoasterControlParams(
        session: DefaultWebSocketSession,
        params: Map<String, Int>?,
        messageId: Int
    ) {
        if (params == null) return

        Log.i("WebSocketServer", "Handle Control Params: $params")
        params.forEach { (nodeName, requestValue) ->
            val command = MachineStateInterpreter.generateControlCommand(
                websocketNameMapping[nodeName]!!,
                if (requestValue > 0) MachineNodeStatus.ON else MachineNodeStatus.OFF,
                requestValue
            )
            coroutineScope.launch {
                sendMessageToMachine(command)
            }
        }
    }

    private suspend fun handleRoasterControlEvent(
        session: DefaultWebSocketSession,
        event: String?,
        messageId: Int
    ) {
        if (event != null) {
            val eventResponse = EventResponse(
                id = messageId,
                data = mapOf("event" to event)
            )
            session.send(Frame.Text(Json.encodeToString(eventResponse)))
        }
    }

    @Suppress("SameParameterValue")
    private fun formatCommand(method: String, commandList: List<ControlCommand>): String {
        val commandDict = ControlCommands(
            m = method,
            c = commandList.size,
            l = commandList
        )

        return Json.encodeToString(commandDict)
    }
}