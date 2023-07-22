package com.example.brewbuddy.domain.recipe_similarity_calculator

import com.example.brewbuddy.data.remote.dto.RecipeMetadataDto
import com.example.brewbuddy.profile.UserPreferences
import kotlin.math.sqrt

object RecipeSimilarityCalculator {

    fun calculateCosineSimilarity(recipe: RecipeMetadataDto, userPreferences: UserPreferences): Double {
        // Treat recipe and userPreferences as binary attribute vectors
        val recipeVector = doubleArrayOf(
            if (recipe.vegan == true) 1.0 else 0.0,
            if (recipe.vegetarian == true) 1.0 else 0.0,
            if (recipe.dairyFree == true) 1.0 else 0.0,
            if (recipe.glutenFree == true) 1.0 else 0.0
        )

        val userPreferencesVector = doubleArrayOf(
            if (userPreferences.vegan) 1.0 else 0.0,
            if (userPreferences.vegetarian) 1.0 else 0.0,
            if (userPreferences.lactoseFree) 1.0 else 0.0,
            if (userPreferences.glutenFree) 1.0 else 0.0
        )

        // Calculate the dot product of the recipe and userPreferences vectors
        var dotProduct = 0.0
        for (i in recipeVector.indices) {
            dotProduct += recipeVector[i] * userPreferencesVector[i]
        }

        // Calculate the magnitudes of the recipe and userPreferences vectors
        val recipeMagnitude = calculateMagnitude(recipeVector)
        val userPreferencesMagnitude = calculateMagnitude(userPreferencesVector)

        // Calculate the cosine similarity
        return if (recipeMagnitude == 0.0 || userPreferencesMagnitude == 0.0) 0.0
        else dotProduct / (recipeMagnitude * userPreferencesMagnitude)
    }

    private fun calculateMagnitude(vector: DoubleArray): Double {
        // Calculate the magnitude of a vector
        var sum = 0.0
        for (value in vector) {
            sum += value * value
        }
        return sqrt(sum)
    }
}