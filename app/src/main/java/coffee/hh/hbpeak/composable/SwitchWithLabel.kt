package coffee.hh.hbpeak.composable

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun SwitchWithLabel(
    modifier: Modifier = Modifier,
    label: String = "text",
    value: String = "",
    state: Boolean = false,
    onStateChange: (Boolean) -> Unit = {},
) {

    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = modifier
            .width(IntrinsicSize.Max)
            .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.medium)
            .clickable(
                interactionSource = interactionSource,
                // This is for removing ripple when Row is clicked
                indication = null,
                role = Role.Switch,
                onClick = {
                    onStateChange(!state)
                }
            )
            .padding(16.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.width(
                IntrinsicSize.Max
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.width(
                IntrinsicSize.Max
            )
        )
        Spacer(modifier = Modifier.padding(start = 8.dp))
        Switch(
            checked = state,
            onCheckedChange = {
                onStateChange(it)
            }
        )
    }
}