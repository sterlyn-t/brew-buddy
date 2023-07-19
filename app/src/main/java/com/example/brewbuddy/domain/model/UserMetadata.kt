package com.example.brewbuddy.domain.model

data class UserMetadata(
    val id: String,
    val likedRecipeIds: List<String>,
    val savedRecipeIds: List<String>
)