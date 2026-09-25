package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = Blue600,
    onPrimary = Color.White,
    primaryContainer = Navy800,
    onPrimaryContainer = Blue100,
    secondary = Gold500,
    onSecondary = Navy900,
    secondaryContainer = Gold600,
    onSecondaryContainer = Gold100,
    tertiary = InfoCyan,
    background = Navy900,
    surface = Slate800,
    onBackground = Slate100,
    onSurface = Slate100,
    surfaceVariant = Slate700,
    onSurfaceVariant = Slate200,
    outline = Slate600
  )

private val LightColorScheme =
  lightColorScheme(
    primary = Blue800,
    onPrimary = Color.White,
    primaryContainer = Blue50,
    onPrimaryContainer = Blue800,
    secondary = Gold600,
    onSecondary = Color.White,
    secondaryContainer = Gold100,
    onSecondaryContainer = Gold600,
    tertiary = InfoCyan,
    background = Slate50,
    surface = Color.White,
    onBackground = Slate900,
    onSurface = Slate900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate700,
    outline = Slate200,
    error = ErrorRed
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep Career Guru Academy branding consistent
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
