package coffee.hh.hbpeak.composable

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coffee.hh.hbpeak.theme.HBPeakTheme
import com.google.android.material.color.MaterialColorUtilitiesHelper
import com.google.android.material.color.MaterialColors


@Composable
fun SwitchWithLabel(
    modifier: Modifier = Modifier,
    label: String = "text",
    value: String = "",
    state: Boolean = false,
    disabled: Boolean = false,
    onStateChange: (Boolean) -> Unit = {},
) {

    val interactionSource = remember { MutableInteractionSource() }
    Row(
        modifier = modifier
            .width(IntrinsicSize.Max)
            .border(
                2.dp,
                if(disabled) MaterialTheme.colorScheme.scrim else
                if (state) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.inverseOnSurface,
                shape = MaterialTheme.shapes.medium
            )
            .clip(MaterialTheme.shapes.medium)
            .background(if(disabled) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceContainer)
            .clickable(
                interactionSource = interactionSource,
                // This is for removing ripple when Row is clicked
                indication = null,
                role = Role.Switch,
                onClick = {
                    if (!disabled) {
                        onStateChange(!state)
                    }
                }

            )
            .padding(16.dp, 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = label, style = MaterialTheme.typography.headlineSmall,
            color = if(disabled) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.headlineSmall,
            color = if(disabled) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
        )
//        Spacer(modifier = Modifier.padding(start = 8.dp))
//        Switch(
//            checked = state,
//            onCheckedChange = {
//                onStateChange(it)
//            }
//        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun SwitchWithLabelPreview() {
    HBPeakTheme {
        Column {
            SwitchWithLabel(
                label = "Switch",
                value = "value",
                state = true,
                onStateChange = {}
            )
            SwitchWithLabel(
                label = "Switch",
                value = "value",
                state = false,
                onStateChange = {}
            )
            SwitchWithLabel(
                label = "Switch",
                value = "value",
                state = false,
                disabled = true,
                onStateChange = {}
            )
        }
    }
}