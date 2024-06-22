package coffee.hh.hbpeak.roasting

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Thermostat
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@SuppressLint("DefaultLocale")
@Preview
@Composable
fun RoastingTemperatureWidget(
    title: String = "Title",
    temperature: Float = 199f,
    rateOfRise: Float = 10f,
) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Outlined.Thermostat,
                    contentDescription = title,
                    modifier = Modifier.size(32.dp),
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .padding(bottom = 8.dp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = String.format("%5.1f", temperature),
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "C.",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = String.format("%4.1f", rateOfRise),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "C/min.",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )
            }
        }

    }
}