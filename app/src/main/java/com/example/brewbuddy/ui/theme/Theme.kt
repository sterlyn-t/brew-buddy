package com.example.brewbuddy.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.compose.ui.graphics.Color


// to change eventually
private val DarkColorScheme = darkColorScheme(
    primary = OrangeBrownMedium,
    onPrimary = Color.White,
    secondary = GreenMedium,
    onSecondary = Color.White,
    tertiary = SlateDark,
    onTertiary = Color.White,
    surface = Cream,
    background = Color.White,
    onBackground = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = OrangeBrownMedium,
    onPrimary = Color.White,
    secondary = GreenMedium,
    onSecondary = Color.White,
    tertiary = SlateDark,
    onTertiary = Color.White,
    surface = Cream,
    background = Color.White,
    onBackground = Color.Black
)

@Composable
fun BrewBuddyTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        // Dynamic color is available on Android 12+
        dynamicColor: Boolean = true,
        content: @Composable () -> Unit
) {
    // temporarily commenting out, causes some problems with setting up the right colors
    //    val colorScheme = when {
    //        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
    //            val context = LocalContext.current
    //            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    //        }
    //
    //        darkTheme -> DarkColorScheme
    //        else -> LightColorScheme
    //    }
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = GreenLight.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
    )
}