package com.example.brewbuddy.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.brewbuddy.ui.theme.TitleLarge

@Composable
fun SettingScreen(menuButton: @Composable () -> Unit) {
    Surface(modifier= Modifier.fillMaxSize()) {
        Column() {
            menuButton()
            TitleLarge(text = "Settings")
        }
    }
}