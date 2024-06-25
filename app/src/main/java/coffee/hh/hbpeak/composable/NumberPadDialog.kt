package coffee.hh.hbpeak.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Preview(widthDp = 1000, heightDp = 1480)
@Composable
fun NumberPadDialog(
    initialValue: String = "999",
    title: String = "Enter Value",
    minValue: Int = 0,
    maxValue: Int = 100,
    showTurnOff: Boolean = true,
    onDismissRequest: () -> Unit = {},
    onValueConfirm: (String) -> Unit = {},
    onTurnOff: (() -> Unit)? = {}
) {
    var value: String by remember { mutableStateOf(initialValue) }
    val hasError = remember { mutableStateOf(false) }

    val handleOnDigitClick = fun(digit: String) {
        if (digit == "DEL") {
            if (value.isNotEmpty()) {
                value = value.dropLast(1)
            }
        } else if (digit == "CLR") {
            value = ""
        } else if (digit == "-10" || digit == "-1" || digit == "+1" || digit == "+10") {
            value = min(max(value.toInt() + digit.toInt(), minValue), maxValue).toString()
        } else if (value.isNotEmpty()) {
            if (
                (value.length == 1 && value == "0")
                || value.length == maxValue.toString().length
                || value.toInt() > maxValue
                || (value.length >= minValue.toString().length && value.toInt() < minValue)
                || ((value + digit).toInt() > maxValue || (value + digit).length > maxValue.toString().length)
            ) {
                value = digit
            } else {
                value += digit
            }
        } else {
            value += digit
        }
    }

    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.padding(24.dp)
            ) {

                Text(
                    title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    NumberButton("-1", onClick = handleOnDigitClick, size = 86.dp)
                    NumberButton("-10", onClick = handleOnDigitClick, size = 86.dp)
                    Text(
                        value,
                        modifier = Modifier
                            .width(140.dp)
                            .height(48.dp),
                        style = MaterialTheme.typography.displayMedium,
                        textAlign = TextAlign.Center
                    )
                    NumberButton("+1", onClick = handleOnDigitClick, size = 86.dp)
                    NumberButton("+10", onClick = handleOnDigitClick, size = 86.dp)
                }

                Text(
                    text = "Value must be between $minValue and $maxValue",
                    color = if (hasError.value)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                )

                NumberPad(onDigitClick = handleOnDigitClick)

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                ) {
                    if (showTurnOff && onTurnOff != null) {
                        FilledTonalButton(onClick = {
                            onTurnOff()
                            onDismissRequest()
                        }, modifier = Modifier.width(240.dp)) {
                            Text(
                                "Turn Off",
                                Modifier
                                    .padding(horizontal = 8.dp)
                                    .height(32.dp),
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    }

                    Button(onClick = {
                        if (value.isNotEmpty() && !hasError.value) {
                            onValueConfirm(value)
                            onDismissRequest()
                        }
                    }, modifier = Modifier.width(240.dp)) {
                        Text(
                            "OK",
                            Modifier
                                .padding(horizontal = 8.dp)
                                .height(32.dp),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NumberPad(onDigitClick: (String) -> Unit) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NumberButton("1", onDigitClick)
                NumberButton("2", onDigitClick)
                NumberButton("3", onDigitClick)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NumberButton("4", onDigitClick)
                NumberButton("5", onDigitClick)
                NumberButton("6", onDigitClick)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NumberButton("7", onDigitClick)
                NumberButton("8", onDigitClick)
                NumberButton("9", onDigitClick)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                NumberButton("CLR", onDigitClick)
                NumberButton("0", onDigitClick)
                NumberButton("DEL", onDigitClick)
            }
        }
    }
}

@Composable
fun NumberButton(text: String, onClick: (String) -> Unit, size: Dp = 96.dp) {
    OutlinedButton(
        onClick = { onClick(text) },
        modifier = Modifier
            .size(size)
            .aspectRatio(1f)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceBright)
        , shape = MaterialTheme.shapes.large
    ) {
        Text(text, style = MaterialTheme.typography.titleLarge)
    }
}