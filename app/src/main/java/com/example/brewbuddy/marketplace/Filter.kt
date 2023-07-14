package com.example.brewbuddy.marketplace

import androidx.compose.runtime.MutableState

data class Filter (
     val filterLabel: String,
     var enabled: Boolean,
)