package coffee.hh.hbpeak.home

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.DownloadDone
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Power
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coffee.hh.hbpeak.theme.HBPeakTheme

data class HomeNavigationItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val onCardClick: () -> Unit
)

@Composable
fun HomeContent(
    contentPadding: PaddingValues = PaddingValues(),
    onRoastingClick: () -> Unit = {},
) {
    val homeNavigationList = listOf(
        HomeNavigationItem(
            title = "Roasting",
            description = "Start your roast!",
            icon = Icons.Outlined.LocalFireDepartment,
            onCardClick = onRoastingClick
        ),
        HomeNavigationItem(
            title = "Auto Profile",
            description = "More roast without sweating.",
            icon = Icons.Outlined.AutoGraph,
            onCardClick = {}
        ),
        HomeNavigationItem(
            title = "Roast Log",
            description = "View your roast history.",
            icon = Icons.Outlined.DownloadDone,
            onCardClick = {}
        ),
        HomeNavigationItem(
            title = "Profile",
            description = "Manage your roast profiles.",
            icon = Icons.Outlined.Folder,
            onCardClick = {}
        ),
        HomeNavigationItem(
            title = "Account",
            description = "Manage your account data.",
            icon = Icons.Outlined.Person,
            onCardClick = {}
        ),
        HomeNavigationItem(
            title = "Settings",
            description = "Machine Setting",
            icon = Icons.Outlined.Settings,
            onCardClick = {}
        ),
        HomeNavigationItem(
            title = "Shutdown",
            description = "Automate Shutdown after Cooling",
            icon = Icons.Outlined.Power,
            onCardClick = {}
        ),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer),
        contentAlignment = Alignment.Center
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = contentPadding,
            modifier = Modifier.padding(16.dp)
        ) {
            items(homeNavigationList) { item ->
                HomeButtonCard(
                    title = item.title,
                    description = item.description,
                    icon = item.icon,
                    onCardClick = item.onCardClick
                )
            }
        }
    }
}

@Composable
private fun HomeButtonCard(
    title: String,
    description: String,
    icon: ImageVector,
    onCardClick: () -> Unit
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(2.dp, Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .padding(64.dp),
        onClick = onCardClick
    ) {
        Column(
            modifier = Modifier.padding(32.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            Text(
                text = description,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(top = 8.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.headlineSmall
            )
        }

    }
}

@Preview(showBackground = true, widthDp = 1200, heightDp = 1920, uiMode = UI_MODE_NIGHT_NO)
@Preview(showBackground = true, widthDp = 1200, heightDp = 1920, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun HomeContentPreview() {
    HBPeakTheme {
        HomeContent()
    }
}