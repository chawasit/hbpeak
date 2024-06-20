package coffee.hh.hbpeak


import android.os.Build
import androidx.annotation.RequiresApi
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
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Collections

class WebSocketServer(
    private val machineState: MachineState,
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
            println("Error during handle websocket request $message {${e.message}}")
        }
    }

    private suspend fun sendDataResponse(session: DefaultWebSocketSession, messageId: Int) {
        val dataResponse = DataResponse(
            id = messageId,
            data = Data(
                BEAN_TEMPERATURE = machineState.beanTemperature.toDouble(),
                EXHAUST_TEMPERATURE = machineState.exhaustTemperature.toDouble(),
                AIR_INLET_TEMP = machineState.airInletTemperature.toDouble(),
                DRUM_TEMP = machineState.drumTemperature.toDouble(),
                BEAN_TEMP_ROR = machineState.beanTemperatureRor.toDouble(),
                EXHAUST_TEMP_ROR = machineState.exhaustTemperatureRor.toDouble(),
                AIR_INLET_TEMP_ROR = machineState.airInletTemperatureRor.toDouble(),
                DRUM_TEMP_ROR = machineState.drumTemperatureRor.toDouble(),
                GAS_LEVEL = machineState.gasLevel.toInt(),
                DRUM_RPM = machineState.drumRpm,
                FAN_LEVEL = machineState.fanLevel,
                COOLING_TRAY_FAN = machineState.coolingTrayFanRunningStatus,
                COOLING_TRAY_STIR = machineState.coolingTrayStirRunningStatus,
                BEAN_HOLDER = machineState.beanHolderOpenStatus,
                DRUM_DOOR = machineState.drumDoorOpenStatus,
                DROP_DOOR = machineState.dropDoorOpenStatus,
                LOADER = machineState.loaderRunningStatus,
                DESTONER = machineState.destonerRunningStatus,
                EXHAUST_FILTER = machineState.exhaustFilterRunningStatus,
                AIR_PRESSURE = machineState.airPressure.toDouble(),
                GAS_PRESSURE = machineState.gasPressure.toDouble()
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

        println("Handle Control Params: $params")
        val requestControls = params.map { (nodeName, requestValue) ->
            ControlCommand(
                t = websocketNameMapping[nodeName] ?: 0,
                s = if (requestValue > 0) MachineStatus.ON else MachineStatus.OFF,
                v = requestValue
            )
        }

        val command = formatCommand(MachineMessageTypes.CONTROL, requestControls)
        sendMessageToMachine(command)
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