package coffee.hh.hbpeak.roasting

import androidx.compose.runtime.MutableState
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

class RoastingGraphViewModel : ViewModel() {
    val entryModel = CartesianChartModelProducer.build()
    var machineState: MutableState<MachineState>? = null
    private val _sensorData = MutableStateFlow<List<List<Float>>>(
        listOf(List(720) { 0f },
            List(720) { 0f },
            List(720) { 0f },
            List(720) { 0f })
    )
    val sensorData: StateFlow<List<List<Float>>> = _sensorData


    init {
        startDataFetching()
    }

    private fun startDataFetching() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                var i = 1
                while (true) {
                    when (machineState?.value?.status) {
                        MachineStatus.IDLE -> {
                            entryModel.tryRunTransaction {
                                lineSeries {
                                    for (data in sensorData.value) {
                                        series(x = data.indices.toList(), y = data.toList())
                                    }
                                }
                            }
                            i++
                        }

                        MachineStatus.ROASTING -> {
                            entryModel.tryRunTransaction {
                                lineSeries {
                                    series(
                                        x = (0..60 * 12).toList(),
                                        y = (0..i).map { it * 5 + 25 })
                                }
                            }
                            i++
                        }

                        null -> {

                        }
                    }

                    if (i > 60 * 12) {
                        i = 0
                        val newData =
                            _sensorData.value.map { it.drop(1) + (0..400).random().toFloat() }
                        _sensorData.value = newData
                    } else {
                        val newData =
                            _sensorData.value.map { it.drop(1) + (0..400).random().toFloat() }
                        _sensorData.value = newData
                    }
                    delay(1000)
                }
            }
        }
    }

    private fun resetGraph() {

    }
}