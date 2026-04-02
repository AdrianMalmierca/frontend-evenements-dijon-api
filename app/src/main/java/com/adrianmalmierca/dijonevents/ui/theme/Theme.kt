package com.adrianmalmierca.dijonevents.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val BurgundyPrimary = Color(0xFF7B1C2E)
val GoldAccent = Color(0xFFD4AF37)
val CreamBackground = Color(0xFFFAF7F2)
val SurfaceLight = Color(0xFFFFFFFF)
val OnPrimary = Color(0xFFFFFFFF)
val PrimaryContainer = Color(0xFFFFDAD9)
val SurfaceVariant = Color(0xFFFFDAD9)
val OnSecondary = Color(0xFF1C1B1F)
val OnBackground = Color(0xFF1C1B1F)

private val LightColorScheme = lightColorScheme(
    primary = BurgundyPrimary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    secondary = GoldAccent,
    onSecondary = OnSecondary,
    background = CreamBackground,
    onBackground = OnBackground,
    surface = SurfaceLight,
    onSurface = OnBackground,
    surfaceVariant = SurfaceVariant,
)

@Composable
fun DijonEventsTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
