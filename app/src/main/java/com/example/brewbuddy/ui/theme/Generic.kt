package com.example.brewbuddy.ui.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TitleLarge(text: String) {
    Text(
        text=text,
        modifier = Modifier.padding(start=24.dp, bottom=12.dp),
        style = MaterialTheme.typography.titleLarge,
    )
}