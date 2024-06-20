package coffee.hh.hbpeak.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NumberPadDialog(
    initialValue: String,
    title: String = "Enter Value",
    minValue: Int? = null,
    maxValue: Int? = null,
    showTurnOff: Boolean = false,
    onDismissRequest: () -> Unit,
    onValueConfirm: (String) -> Unit,
    onTurnOff: (() -> Unit)? = null
) {
    var value by remember { mutableStateOf(initialValue) }
    val hasError = remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(title) },
        text = {
            Box(modifier = Modifier.padding(8.dp), contentAlignment = Alignment.Center) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .sizeIn(minHeight = 48.dp),
                        fontSize = 36.sp,
                        textAlign = TextAlign.Center
                    )

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

                    NumberPad(
                        onDigitClick = { digit ->
                            if (digit == "DEL") {
                                if (value.isNotEmpty()) {
                                    value = value.dropLast(1)
                                }
                            } else if (digit == "CLR") {
                                value = ""
                            } else {
                                value += digit
                            }

                            value = value.toIntOrNull()?.toString() ?: "0"
                            if (value.isNotEmpty()) {
                                val intValue = value.toIntOrNull()
                                if ((minValue != null && intValue != null && intValue < minValue) ||
                                    (maxValue != null && intValue != null && intValue > maxValue)
                                ) {
                                    hasError.value = true
                                } else {
                                    hasError.value = false
                                }
                            }
                        }
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (value.isNotEmpty() && !hasError.value) {
                    onValueConfirm(value)
                    onDismissRequest()
                }
            }) {
                Text(
                    "OK",
                    Modifier
                        .padding(horizontal = 8.dp)
                        .height(32.dp),
                    fontSize = 24.sp
                )
            }
        },
        dismissButton = {
            Row {
                if (showTurnOff && onTurnOff != null) {
                    FilledTonalButton(onClick = {
                        onTurnOff()
                        onDismissRequest()
                    }) {
                        Text(
                            "Turn Off",
                            Modifier
                                .padding(horizontal = 8.dp)
                                .height(32.dp),
                            fontSize = 24.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
                OutlinedButton(onClick = onDismissRequest) {
                    Text(
                        "Cancel",
                        Modifier
                            .padding(horizontal = 8.dp)
                            .height(32.dp),
                        fontSize = 24.sp
                    )
                }
            }
        }
    )
}

@Composable
fun NumberPad(onDigitClick: (String) -> Unit) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NumberButton("1", onDigitClick)
                NumberButton("2", onDigitClick)
                NumberButton("3", onDigitClick)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NumberButton("4", onDigitClick)
                NumberButton("5", onDigitClick)
                NumberButton("6", onDigitClick)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NumberButton("7", onDigitClick)
                NumberButton("8", onDigitClick)
                NumberButton("9", onDigitClick)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                NumberButton("CLR", onDigitClick)
                NumberButton("0", onDigitClick)
                NumberButton("DEL", onDigitClick)
            }
        }
    }
}

@Composable
fun NumberButton(text: String, onClick: (String) -> Unit) {
    OutlinedButton(
        onClick = { onClick(text) },
        modifier = Modifier
            .size(96.dp)
            .aspectRatio(1f)
    ) {
        Text(text, fontSize = 24.sp)
    }
}