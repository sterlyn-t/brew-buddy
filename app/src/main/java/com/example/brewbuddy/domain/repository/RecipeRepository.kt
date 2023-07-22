package com.example.brewbuddy.domain.repository

import com.example.brewbuddy.data.remote.dto.RecipeDto
import com.example.brewbuddy.data.remote.dto.RecipeMetadataDto
import com.example.brewbuddy.domain.model.Recipe
import com.example.brewbuddy.domain.model.User

interface RecipeRepository {

    suspend fun getRecipes(query: String?): List<RecipeMetadataDto>

    suspend fun getRecipeById(recipeId: String): RecipeDto

    suspend fun getPopular(): List<RecipeMetadataDto>

    suspend fun getAllRecipes(): List<RecipeMetadataDto>
}