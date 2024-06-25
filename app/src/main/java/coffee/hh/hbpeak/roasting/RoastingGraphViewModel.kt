package coffee.hh.hbpeak.roasting

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coffee.hh.hbpeak.MachineState
import coffee.hh.hbpeak.MachineStatus
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    var machineState: MutableState<MachineState>? = null
    private var startTimer = System.currentTimeMillis()
    private val _graphData = MutableStateFlow<List<SensorDataFrame>>(listOf())
    val graphData: StateFlow<List<SensorDataFrame>> = _graphData


    init {
        startDataFetching()
    }

    fun startTimer () {
        resetTimer()
    }

    fun resetTimer () {
        startTimer = System.currentTimeMillis()
    }

    private fun startDataFetching() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                while (true) {
                    val currentTime = System.currentTimeMillis()
                    _graphData.value += SensorDataFrame(
                        time = (currentTime - startTimer) / 1000f,
                        point = SensorDataPoint(
                            beanTemperature = machineState?.value?.beanTemperature!!,
                            drumTemperature = machineState?.value?.drumTemperature!!,
                            airInletTemperature = machineState?.value?.airInletTemperature!!,
                            exhaustTemperature = machineState?.value?.exhaustTemperature!!,
                            beanTemperatureRor = machineState?.value?.beanTemperatureRor!!,
                            drumTemperatureRor = machineState?.value?.drumTemperatureRor!!,
                            airInletTemperatureRor = machineState?.value?.airInletTemperatureRor!!,
                            exhaustTemperatureRor = machineState?.value?.exhaustTemperatureRor!!
                        )
                    )

                    when (machineState?.value?.status) {
                        MachineStatus.IDLE -> {

                        }
                        MachineStatus.ROASTING -> {

                        }
                        null -> {

                        }
                    }
                    delay(500)
                }
            }
        }
    }

    private fun resetGraph() {

    }
}