package coffee.hh.hbpeak.roasting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.Snapshot.Companion.withMutableSnapshot
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import coffee.hh.hbpeak.MachineState
import kotlin.math.floor

data class SensorDataPoint(
    val beanTemperature: Float,
    val drumTemperature: Float,
    val airInletTemperature: Float,
    val exhaustTemperature: Float,
    val beanTemperatureRor: Float,
    val drumTemperatureRor: Float,
    val airInletTemperatureRor: Float,
    val exhaustTemperatureRor: Float
)

data class SensorDataFrame(
    val time: Float,
    val point: SensorDataPoint
)

@OptIn(SavedStateHandleSaveableApi::class)
class RoastingGraphViewModel(savedStateHandle: SavedStateHandle = SavedStateHandle()) : ViewModel() {
    private var _startTimer by savedStateHandle.saveable {
        mutableLongStateOf(System.currentTimeMillis())
    }

    var graphData: List<SensorDataFrame> by savedStateHandle.saveable {
        mutableStateOf(listOf())
    }

    init {
        print("Init RoastingGraphViewModel")
    }

    fun elapseMinutes(currentTime: Long): Float {
        return (currentTime - _startTimer) / 1000f / 60f
    }

    fun elapseMinutes(): Float {
        val currentTime = System.currentTimeMillis()
        return elapseMinutes(currentTime)
    }

    fun startRoasting() {
        resetAll()
    }

    fun resetAll() {
        resetTimer()
        resetGraph()
    }

    fun resetTimer() {
        withMutableSnapshot {
            _startTimer = System.currentTimeMillis()
        }
    }

    fun dataFetching(machineState: MachineState) {
        withMutableSnapshot {
            graphData += SensorDataFrame(
                time = elapseMinutes(),
                point = SensorDataPoint(
                    beanTemperature = machineState.beanTemperature,
                    drumTemperature = machineState.drumTemperature,
                    airInletTemperature = machineState.airInletTemperature,
                    exhaustTemperature = machineState.exhaustTemperature,
                    beanTemperatureRor = machineState.beanTemperatureRor,
                    drumTemperatureRor = machineState.drumTemperatureRor,
                    airInletTemperatureRor = machineState.airInletTemperatureRor,
                    exhaustTemperatureRor = machineState.exhaustTemperatureRor
                )
            )
        }
    }

    private fun resetGraph() {
        withMutableSnapshot {
            graphData = listOf()
        }
    }

    @Composable
    fun getFormattedTime(): String {
        val elapsedTime = elapseMinutes()
        val minutes = floor(elapsedTime).toInt()
        val seconds = ((elapsedTime - minutes) * 60).toInt()

        return "${minutes}:${seconds}"
    }
}

