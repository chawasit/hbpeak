package coffee.hh.hbpeak.roasting

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Paint
import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coffee.hh.hbpeak.MachineState
import coffee.hh.hbpeak.theme.HBPeakTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RoastingGraph(
    machineState: MutableState<MachineState> = mutableStateOf(MachineState()),
    roastingGraphViewModel: RoastingGraphViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()

    val backgroundColor = MaterialTheme.colorScheme.surfaceBright
    val textColor = MaterialTheme.colorScheme.onSurface
    val lineColor = MaterialTheme.colorScheme.onSurface

    val graphAppearance = remember {
        mutableStateOf(
            GraphAppearance(
                backgroundColor,
                textColor,
                lineColor
            )
        )
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            roastingGraphViewModel.resetTimer()
            while (true) {
                roastingGraphViewModel.dataFetching(machineState.value)
                if (graphAppearance.value.xMax < roastingGraphViewModel.elapseMinutes()) {
                    graphAppearance.value = graphAppearance.value.copy(xMax = roastingGraphViewModel.elapseMinutes().toInt() + 2)
                }
                delay(500)
            }
        }
    }

    val graphData = roastingGraphViewModel.graphData.collectAsState()

    val beanTemperatureChartData = graphData.value.map { frame ->
        PointF(
            frame.time,
            frame.point.beanTemperature
        )
    }

    val drumTemperatureChartData = graphData.value.map { frame ->
        PointF(
            frame.time,
            frame.point.drumTemperature
        )
    }

    val airInletTemperatureChartData = graphData.value.map { frame ->
        PointF(
            frame.time,
            frame.point.airInletTemperature
        )
    }

    val exhaustTemperatureChartData = graphData.value.map { frame ->
        PointF(
            frame.time,
            frame.point.exhaustTemperature
        )
    }

    val beanTemperatureRorChartData = graphData.value.map { frame ->
        PointF(
            frame.time,
            frame.point.beanTemperatureRor
        )
    }

    val drumTemperatureRorChartData = graphData.value.map { frame ->
        PointF(
            frame.time,
            frame.point.drumTemperatureRor
        )
    }

    val airInletTemperatureRorChartData = graphData.value.map { frame ->
        PointF(
            frame.time,
            frame.point.airInletTemperatureRor
        )
    }

    val exhaustTemperatureRorChartData = graphData.value.map { frame ->
        PointF(
            frame.time,
            frame.point.exhaustTemperatureRor
        )
    }

    val chartData = listOf(
        ChartData(
            points = beanTemperatureChartData,
            chartAppearance = ChartAppearance(color = Color.Blue)
        ),
        ChartData(
            points = drumTemperatureChartData,
            chartAppearance = ChartAppearance(color = Color.Green)
        ),
        ChartData(
            points = airInletTemperatureChartData,
            chartAppearance = ChartAppearance(color = Color.Red)
        ),
        ChartData(
            points = exhaustTemperatureChartData,
            chartAppearance = ChartAppearance(color = Color.Yellow)
        ),
        ChartData(
            points = beanTemperatureRorChartData,
            chartAppearance = ChartAppearance(color = Color.Blue.copy(alpha = 0.8f), onSecondAxis = true)
        ),
        ChartData(
            points = drumTemperatureRorChartData,
            chartAppearance = ChartAppearance(color = Color.Green.copy(alpha = 0.8f), onSecondAxis = true)
        ),
        ChartData(
            points = airInletTemperatureRorChartData,
            chartAppearance = ChartAppearance(color = Color.Red.copy(alpha = 0.8f), onSecondAxis = true)
        ),
        ChartData(
            points = exhaustTemperatureRorChartData,
            chartAppearance = ChartAppearance(color = Color.Yellow.copy(alpha = 0.8f), onSecondAxis = true)
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LineChart(
            modifier = Modifier,
            chartData = chartData,
            graphAppearance = graphAppearance.value
        )
    }
}


@Composable
fun LineChart(
    modifier: Modifier,
    chartData: List<ChartData> = listOf(),
    graphAppearance: GraphAppearance
) {
    val density = LocalDensity.current
    val tempTextPaint = remember(density) {
        Paint().apply {
            color = graphAppearance.textColor.toArgb()
            textAlign = Paint.Align.CENTER
            textSize = density.run { graphAppearance.textSize.toPx() }
        }
    }

    val rorTextPaint = remember(density) {
        Paint().apply {
            color = graphAppearance.textColor.toArgb()
            textAlign = Paint.Align.CENTER
            textSize = density.run { graphAppearance.text2Size.toPx() }
        }
    }

    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceBright)
            .padding(horizontal = 32.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize(),
        ) {
            val startX = graphAppearance.paddingSpace.toPx()
            val startY = graphAppearance.paddingSpace.toPx()
            val endX = size.width - graphAppearance.paddingSpace.toPx()
            val endY = size.height - graphAppearance.paddingSpace.toPx()

            /** Draw Axis **/
            val linePath = Path().apply {
                reset()
                moveTo(startX, startY)
                lineTo(startX, endY)
                lineTo(endX, endY)
            }
            drawPath(
                linePath,
                color = graphAppearance.lineColor,
                style = Stroke(
                    width = graphAppearance.graphAxisThickness,
                    cap = StrokeCap.Round
                )
            )

            val xAxisSpace =
                (size.width - graphAppearance.paddingSpace.toPx() * 2) / graphAppearance.xMax
            val yAxisSpace =
                (size.height - graphAppearance.paddingSpace.toPx() * 2) / graphAppearance.yMax
            val y2AxisSpace =
                (size.height - graphAppearance.paddingSpace.toPx() * 2) / graphAppearance.y2Max


            /** placing x axis points */
            for (i in (graphAppearance.xMin..graphAppearance.xMax step graphAppearance.horizontalStep)) {
                drawContext.canvas.nativeCanvas.drawText(
                    "$i",
                    startX + xAxisSpace * (i),
                    size.height - graphAppearance.paddingSpace.toPx() / 4,
                    tempTextPaint
                )

                val lineXPath = Path().apply {
                    reset()
                    moveTo(startX + xAxisSpace * i, startY)
                    lineTo(startX + xAxisSpace * i, endY)
                }
                drawPath(
                    lineXPath,
                    color = graphAppearance.lineColor.copy(alpha = 0.5f),
                    style = Stroke(
                        width = graphAppearance.graphAxisThickness / 2,
                        cap = StrokeCap.Round
                    )
                )
            }
            /** placing y axis points */
            for (i in (graphAppearance.yMin.toInt()..graphAppearance.yMax.toInt() step graphAppearance.verticalStep)) {
                drawContext.canvas.nativeCanvas.drawText(
                    "$i C",
                    graphAppearance.paddingSpace.toPx() / 4,
                    endY - yAxisSpace * i + graphAppearance.textSize.toPx() / 4,
                    tempTextPaint
                )

                val lineYPath = Path().apply {
                    reset()
                    moveTo(startX, endY - yAxisSpace * i)
                    lineTo(endX, endY - yAxisSpace * i)
                }
                drawPath(
                    lineYPath,
                    color = graphAppearance.lineColor.copy(alpha = 0.5f),
                    style = Stroke(
                        width = graphAppearance.graphAxisThickness / 2,
                        cap = StrokeCap.Round
                    )
                )
            }
            /** placing y2 axis points */
            for (i in (graphAppearance.y2Min.toInt()..graphAppearance.y2Max.toInt() step graphAppearance.verticalStep2)) {
                drawContext.canvas.nativeCanvas.drawText(
                    "${i} c/min",
                    size.width - graphAppearance.paddingSpace.toPx() / 4f,
                    endY - y2AxisSpace * i + graphAppearance.textSize.toPx() / 4,
                    rorTextPaint
                )
            }

            for (chart in chartData) {
                val controlPoints1 = mutableListOf<PointF>()
                val controlPoints2 = mutableListOf<PointF>()
                val coordinates = mutableListOf<PointF>()

                val points = chart.points
                val ySpace = if (chart.chartAppearance.onSecondAxis) y2AxisSpace else yAxisSpace

                /** placing our x axis points */
                for (i in points.indices) {
                    val x1 = startX + (xAxisSpace * points[i].x)
                    val y1 = endY - (ySpace * points[i].y)
                    coordinates.add(PointF(x1, y1))
                }

                /** calculating the connection points */
                for (i in 1 until coordinates.size) {
                    controlPoints1.add(
                        PointF(
                            (coordinates[i].x + coordinates[i - 1].x) / 2,
                            coordinates[i - 1].y
                        )
                    )
                    controlPoints2.add(
                        PointF(
                            (coordinates[i].x + coordinates[i - 1].x) / 2,
                            coordinates[i].y
                        )
                    )
                }

                if (coordinates.isEmpty())
                    continue

                /** drawing the path */
                val stroke = Path().apply {
                    reset()
                    moveTo(coordinates.first().x, coordinates.first().y)
                    for (i in 1 until coordinates.size) {
                        cubicTo(
                            controlPoints1[i - 1].x, controlPoints1[i - 1].y,
                            controlPoints2[i - 1].x, controlPoints2[i - 1].y,
                            coordinates[i].x, coordinates[i].y
                        )
                    }
                }

                drawPath(
                    stroke,
                    color = chart.chartAppearance.color,
                    style = Stroke(
                        width = chart.chartAppearance.lineThickness,
                        cap = StrokeCap.Round
                    )
                )
            }
        }
    }

}

data class ChartData(
    val points: List<PointF> = listOf(),
    val chartAppearance: ChartAppearance
)

data class ChartAppearance(
    val lineThickness: Float = 4f,
    val isColorAreaUnderChart: Boolean = false,
    val color: Color = Color.Red,
    val showPrediction: Boolean = false,
    val onSecondAxis: Boolean = false
)

data class GraphAppearance(
    val backgroundColor: Color,
    val textColor: Color,
    val lineColor: Color,
    val graphAxisThickness: Float = 4f,
    val textSize: TextUnit = 24.sp,
    val text2Size: TextUnit = 18.sp,
    val xMax: Int = 12,
    val xMin: Int = 0,
    val yMax: Float = 400f,
    val yMin: Float = 0f,
    val y2Max: Float = 40f,
    val y2Min: Float = 0f,
    val paddingSpace: Dp = 64.dp,
    val horizontalStep: Int = 1,
    val verticalStep: Int = 25,
    val verticalStep2: Int = 5
)


@Preview(
    name = "Roasting Graph light theme",
    widthDp = 1200,
    heightDp = 1920,
    showBackground = true,
    apiLevel = 34,
    uiMode = UI_MODE_NIGHT_NO
)
@Preview(
    name = "Roasting Graph dark theme",
    widthDp = 1200,
    heightDp = 1920,
    showBackground = true,
    apiLevel = 34,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun RoastingGraphPreview() {
    HBPeakTheme {
        RoastingGraph()
    }
}