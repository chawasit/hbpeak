package coffee.hh.hbpeak

import android.animation.ObjectAnimator
import android.hardware.usb.UsbManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import coffee.hh.hbpeak.theme.HBPeakTheme
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import com.hoho.android.usbserial.util.SerialInputOutputManager
import io.ktor.server.netty.NettyApplicationEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var webSocketServer: NettyApplicationEngine
    private var serialPort: UsbSerialPort? = null
    private val coroutineScope = CoroutineScope(Dispatchers.IO + Job())
    private val machineState = mutableStateOf(MachineState())
    private val commandQueue = ArrayDeque<String>()
    private var isSending = false
    private var serialReadBuffer = StringBuilder()
    private var isSerialProcessing: Boolean = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            val fadeOut = ObjectAnimator.ofFloat(
                splashScreenViewProvider.view,
                View.ALPHA,
                1f, 0f
            )
            fadeOut.interpolator = AccelerateInterpolator()
            fadeOut.duration = 1000L

            fadeOut.doOnEnd {
                splashScreenViewProvider.remove()
            }

            fadeOut.start()
        }

        // Initialize WebSocket server
        webSocketServer = WebSocketServer(machineState, coroutineScope, ::enqueueCommand).create()
        webSocketServer.start()

        // Initialize Serial connection
        initSerial()

        // Start routine to request machine status
        startStatusRequestRoutine()

        setContent {
            HBPeakTheme {
                HBPeakNavHost(machineState, ::enqueueCommand)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocketServer.stop()
        serialPort?.close()
        coroutineScope.cancel()
    }

    private fun initSerial() {
        val usbManager = getSystemService(USB_SERVICE) as UsbManager
        val availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(usbManager)

        if (availableDrivers.isNotEmpty()) {
            var preferDriver = availableDrivers[0]
            for (driver in availableDrivers) {
                Log.d("SerialProcessor", "Driver: ${driver.device.deviceName}")
                if (driver.device.deviceName == "CP2102 USB to UART Bridge Controller") {
                    preferDriver = driver
                    break
                }
            }
            val driver = preferDriver
            val connection = usbManager.openDevice(driver.device)
            if (connection == null) {
                Log.e("SerialProcessor","Connection is null")
                return
            }

            serialPort = driver.ports[0]
            serialPort?.open(connection)
            serialPort?.setParameters(57600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE)

            val usbIoManager = SerialInputOutputManager(serialPort, serialListener)
            Executors.newSingleThreadExecutor().submit(usbIoManager)
        } else {
            Log.e("SerialProcessor","No Available Driver")
        }
    }

    private val serialListener = object : SerialInputOutputManager.Listener {
        override fun onNewData(data: ByteArray) {
            coroutineScope.launch {
                serialReadBuffer.append(String(data))
                processBuffer()
            }
        }

        override fun onRunError(e: Exception) {
            Log.e("SerialProcessor","Serial Error: ${e.message}")
        }
    }

    private fun processBuffer() {
        if (isSerialProcessing) return

        while (serialReadBuffer.contains("#")) {
            isSerialProcessing = true
            val endIndex = serialReadBuffer.indexOf("#")
            if (endIndex != -1) {
                val message = serialReadBuffer.substring(0, endIndex)
                serialReadBuffer.delete(0, endIndex + 1)
                Log.d("SerialProcessor","Received: $message")
                machineState.value = MachineStateInterpreter.interpretMessage(machineState.value, message)
            }
        }
        isSerialProcessing = false
    }

    private fun startStatusRequestRoutine() {
        coroutineScope.launch {
            while (true) {
                if (commandQueue.isEmpty()) {
                    val statusRequestCommand = MachineStateInterpreter.generateStatusRequestCommand()
                    enqueueCommand(statusRequestCommand)
                }
                delay(250)
            }
        }
    }

    private fun enqueueCommand(command: String) {
        Log.i("SerialProcessor" ,"Enqueue Command: $command")
        commandQueue.addLast(command)
        processCommandQueue()
    }

    private fun processCommandQueue() {
        if (isSending) return

        coroutineScope.launch {
            while (commandQueue.isNotEmpty()) {
                isSending = true
                val command = commandQueue.removeFirst()
                sendMessageToMachine(command)
                delay(100)
            }
            isSending = false
        }
    }


    private fun sendMessageToMachine(command: String) {
        coroutineScope.launch {
            val padCommand = ">${command}#"
            serialPort?.write(padCommand.toByteArray(), 1000)
        }
    }
}