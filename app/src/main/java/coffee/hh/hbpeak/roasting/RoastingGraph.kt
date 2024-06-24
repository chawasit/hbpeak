package coffee.hh.hbpeak.roasting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coffee.hh.hbpeak.MachineState
import coffee.hh.hbpeak.MachineStatus
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberEndAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberTopAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineSpec
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.shader.color
import com.patrykandpatrick.vico.core.cartesian.data.AxisValueOverrider
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.common.shader.DynamicShader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.random.Random


@Composable
fun RoastingGraph(machineState: MutableState<MachineState>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LineChart(machineState)
    }
}

@Composable
fun LineChart(machineState: MutableState<MachineState>) {
    val viewModel: RoastingGraphViewModel = viewModel()
    viewModel.machineState = machineState
    val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow)
    val legends = listOf("Sensor 1", "Sensor 2", "Sensor 3", "Sensor 4")

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            legends.forEachIndexed { index, legend ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(colors[index])
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = legend)
                }
            }
        }

        CartesianChartHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            chart = rememberCartesianChart(
                rememberLineCartesianLayer(
                    listOf(
                        rememberLineSpec(DynamicShader.color(MaterialTheme.colorScheme.primary)),
                        rememberLineSpec(DynamicShader.color(MaterialTheme.colorScheme.error)),
                        rememberLineSpec(DynamicShader.color(MaterialTheme.colorScheme.secondary)),
                        rememberLineSpec(DynamicShader.color(MaterialTheme.colorScheme.scrim))
                    ),
                    axisValueOverrider = AxisValueOverrider.fixed(0f,60*12f, 0f , 400f)
                ),
                startAxis = rememberStartAxis(),
                bottomAxis = rememberBottomAxis(guideline = null),

            ),
            modelProducer = viewModel.entryModel,
            scrollState = rememberVicoScrollState(false),
            zoomState = rememberVicoZoomState(true),
            getXStep = { 60f },
            runInitialAnimation = false
        )
    }
}
