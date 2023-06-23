package com.example.brewbuddy.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.example.brewbuddy.ui.theme.Screen
import com.example.brewbuddy.ui.theme.TitleLarge

@Composable
fun PreferencesScreen(menuButton: @Composable () -> Unit) {
    Screen() {
        Column() {
            menuButton()
            TitleLarge(text = "Preferences")
        }
    }
}