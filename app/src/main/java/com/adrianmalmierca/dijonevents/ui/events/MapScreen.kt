package com.adrianmalmierca.dijonevents.ui.events

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

// Centro de Dijon
private val DijonCenter = LatLng(47.3220, 5.0415)

@Composable
fun MapScreen(viewModel: EventsViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(DijonCenter, 13f)
    }
    var selectedEvent by remember { mutableStateOf<com.adrianmalmierca.dijonevents.data.model.EventDto?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = false)
        ) {
            uiState.events
                .filter { it.latitude != null && it.longitude != null }
                .forEach { event ->
                    Marker(
                        state = MarkerState(
                            position = LatLng(event.latitude!!, event.longitude!!)
                        ),
                        title = event.title,
                        snippet = event.locationName,
                        onClick = {
                            selectedEvent = event
                            false
                        }
                    )
                }
        }

        // Info card del evento seleccionado
        selectedEvent?.let { event ->
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = event.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = { selectedEvent = null }) {
                            Text("✕")
                        }
                    }
                    event.locationName?.let {
                        Text("📍 $it", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                    }
                    event.dateStart?.let {
                        Text("📅 ${it.take(10)}", fontSize = 13.sp, color = MaterialTheme.colorScheme.secondary)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { viewModel.toggleFavorite(event) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val isFav = viewModel.isFavorite(event.uid)
                        Text(if (isFav) "❤️ Retirer des favoris" else "🤍 Ajouter aux favoris")
                    }
                }
            }
        }

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
