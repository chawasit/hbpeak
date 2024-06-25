package coffee.hh.hbpeak.roasting

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coffee.hh.hbpeak.MachineState
import coffee.hh.hbpeak.MachineStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

class RoastingGraphViewModel : ViewModel() {
    private var _startTimer = System.currentTimeMillis()
    private val _graphData: MutableStateFlow<List<SensorDataFrame>> = MutableStateFlow(listOf())
    val graphData: StateFlow<List<SensorDataFrame>> = _graphData.asStateFlow()

    init {
        print("Init RoastingGraphViewModel")
        startTimer()
    }

    fun startTimer() {
        resetTimer()
    }

    fun elapseMinutes(currentTime: Long): Float {
        return (currentTime - _startTimer) / 1000f / 60f
    }

    fun elapseMinutes(): Float {
        val currentTime = System.currentTimeMillis()
        return elapseMinutes(currentTime)
    }

    fun resetTimer() {
        _startTimer = System.currentTimeMillis()
        _graphData.value = listOf()
    }

    fun dataFetching(machineState: MachineState) {
        _graphData.update { current ->
            val sensorDataFrames = current + SensorDataFrame(
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
            sensorDataFrames
        }

        when (machineState.status) {
            MachineStatus.IDLE -> {

            }

            MachineStatus.ROASTING -> {

            }

            null -> {

            }
        }
    }
}

private fun resetGraph() {

}