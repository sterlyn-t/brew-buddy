package com.example.brewbuddy.domain.repository

import com.example.brewbuddy.data.remote.dto.RecipeDto
import com.example.brewbuddy.data.remote.dto.RecipeMetadataDto
import com.example.brewbuddy.data.remote.dto.UserMetadataDto

interface RecipeRepository {

    suspend fun getRecipes(): List<RecipeMetadataDto>

    suspend fun getRecipeById(recipeId: String): RecipeDto

    suspend fun getUserMetadata(userId: String): UserMetadataDto
}