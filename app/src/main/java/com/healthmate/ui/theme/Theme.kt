package com.healthmate.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF80A9FF),
    secondary = Color(0xFFB0C8F5),
    tertiary = Color(0xFF9FA8DA),

    background = Color(0xFF101827),
    surface = Color(0xFF182438),
    surfaceVariant = Color(0xFF26364D),

    onPrimary = Color(0xFF10264A),
    onSecondary = Color(0xFF17243A),
    onTertiary = Color.White,

    onBackground = Color(0xFFE8EEFA),
    onSurface = Color(0xFFE8EEFA),
    onSurfaceVariant = Color(0xFFB8C5D9),

    outline = Color(0xFF52647D)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0755D9),
    secondary = Color(0xFF59647A),
    tertiary = Color(0xFF496DB5),

    background = Color(0xFFF7F8FF),
    surface = Color.White,
    surfaceVariant = Color(0xFFE6EEFF),

    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,

    onBackground = Color(0xFF102641),
    onSurface = Color(0xFF102641),
    onSurfaceVariant = Color(0xFF59647A),

    outline = Color(0xFFE8EEFA)
)

@Composable
fun HealthMateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}