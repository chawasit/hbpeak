package coffee.hh.hbpeak.home

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.outlined.Assessment
import androidx.compose.material.icons.outlined.AutoGraph
import androidx.compose.material.icons.outlined.Fireplace
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.Person
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

data class HomeNavigationItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val onCardClick: () -> Unit
)
@Preview
@Composable
fun HomeContent(contentPadding: PaddingValues = PaddingValues()) {

    val homeNavigationList = listOf(
        HomeNavigationItem(
            title = "Roasting",
            description = "Start your roast!",
            icon = Icons.Outlined.Fireplace,
            onCardClick = {}
        ),
        HomeNavigationItem(
            title = "Profile",
            description = "Manage your roast profiles.",
            icon = Icons.Outlined.AutoGraph,
            onCardClick = {}
        ),
        HomeNavigationItem(
            title = "Inventory",
            description = "Manage your green inventory.",
            icon = Icons.Outlined.Inventory,
            onCardClick = {}
        ),
        HomeNavigationItem(
            title = "Cupping",
            description = "Quality Assessment on Hand",
            icon = Icons.Outlined.Assessment,
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

    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = contentPadding,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        items(homeNavigationList) { item ->
            HomeButtonCard(
                title=item.title,
                description = item.description,
                icon = item.icon,
                onCardClick = item.onCardClick)
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
        ),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        onClick = onCardClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(32.dp),
                )
                Text(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                    textAlign = TextAlign.Start,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Text(
                text = description,
                modifier = Modifier
                    .align(Alignment.Start).padding(top = 8.dp),
                color = Color.Gray,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
            )
        }

    }
}