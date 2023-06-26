package com.example.brewbuddy.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun currentRoute(navController: NavHostController): String? {
    return navController.currentBackStackEntry?.destination?.route
}