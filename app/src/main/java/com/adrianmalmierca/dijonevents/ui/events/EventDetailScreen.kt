package com.adrianmalmierca.dijonevents.ui.events

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.adrianmalmierca.dijonevents.data.model.EventDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    uid: String,
    viewModel: EventsViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val event = uiState.events.find { it.uid == uid }
        ?: uiState.favorites.find { it.uid == uid }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(event?.title ?: "Événement", maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    event?.let {
                        val isFav = viewModel.isFavorite(it.uid)
                        IconButton(onClick = { viewModel.toggleFavorite(it) }) {
                            Icon(
                                imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = if (isFav) "Retirer des favoris" else "Ajouter aux favoris",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (event == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            EventDetailContent(
                event = event,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@Composable
fun EventDetailContent(event: EventDto, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        if (!event.imageUrl.isNullOrEmpty()) {
            AsyncImage(
                model = event.imageUrl,
                contentDescription = event.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentScale = ContentScale.Crop
            )
        }

        Column(modifier = Modifier.padding(20.dp)) {

            Text(
                text = event.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (event.dateStart != null) {
                DetailRow(
                    icon = "📅",
                    label = "Date",
                    value = buildString {
                        append(event.dateStart.take(10))
                        if (event.dateEnd != null) append(" → ${event.dateEnd.take(10)}")
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (event.dateStart != null && event.dateStart.length > 10) {
                val time = event.dateStart.substring(11, 16)
                DetailRow(icon = "🕐", label = "Heure", value = time)
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (!event.locationName.isNullOrEmpty()) {
                DetailRow(icon = "🏛️", label = "Lieu", value = event.locationName)
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (!event.address.isNullOrEmpty()) {
                DetailRow(icon = "📍", label = "Adresse", value = event.address)
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (!event.city.isNullOrEmpty()) {
                DetailRow(icon = "🏙️", label = "Ville", value = event.city)
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (event.categories.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                DetailRow(
                    icon = "🏷️",
                    label = "Catégories",
                    value = event.categories.joinToString(", ")
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            if (!event.description.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Description",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = event.description,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f)
                )
            }
        }
    }
}

@Composable
fun DetailRow(icon: String, label: String, value: String) {
    Row(verticalAlignment = Alignment.Top) {
        Text(icon, fontSize = 18.sp, modifier = Modifier.width(32.dp))
        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}