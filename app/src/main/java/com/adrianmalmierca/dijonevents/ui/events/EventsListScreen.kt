package com.adrianmalmierca.dijonevents.ui.events

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.foundation.clickable


@OptIn(ExperimentalMaterial3Api::class) //Because I use OutlinedTextField, so we indicate to the compiler that we know is experimental
@Composable
fun EventsListScreen(viewModel: EventsViewModel, onNavigateToDetail: (String) -> Unit) {
    //collectAsState: Transforms the `Flow` into a Compose State
    val uiState by viewModel.uiState.collectAsState() //listen the vm state and give me the actual value, recharging the ui
    var searchQuery by remember { mutableStateOf("") } //Keeps the text of the textfield as reactive and it keeps

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                if (it.length > 2 || it.isEmpty()) viewModel.loadEvents(it.ifEmpty { null }) //is you dont write, it returns null, otherwise, it returns the text
            },
            placeholder = { Text("Rechercher un événement...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            singleLine = true
        )

        when {
            uiState.isLoading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            uiState.error != null -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { Text(uiState.error ?: "", color = MaterialTheme.colorScheme.error) }

            else -> LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.events) { event ->
                    EventCard(
                        event = event,
                        isFavorite = viewModel.isFavorite(event.uid),
                        onToggleFavorite = { viewModel.toggleFavorite(event) },
                        onNavigateToDetail = { onNavigateToDetail(event.uid) }
                    )
                }
            }
        }
    }
}

@Composable
fun EventCard(
    event: EventDto,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onNavigateToDetail: () -> Unit
){
    Card(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToDetail() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ){
        Column {
            if (!event.imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = event.imageUrl,
                    contentDescription = event.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = event.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (isFavorite) "Retirer des favoris" else "Ajouter aux favoris",
                            tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                event.locationName?.let {
                    Text(text = "📍 $it", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                }

                event.dateStart?.let {
                    val date = it.take(10)
                    Text(text = "📅 $date", fontSize = 13.sp, color = MaterialTheme.colorScheme.secondary)
                }

                if (event.categories.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = event.categories.take(3).joinToString(" · "),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}