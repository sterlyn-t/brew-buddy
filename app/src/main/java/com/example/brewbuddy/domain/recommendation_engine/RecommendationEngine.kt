package com.example.brewbuddy.domain.recommendation_engine

import android.util.Log
import com.example.brewbuddy.data.remote.dto.toRecipeMetadata
import com.example.brewbuddy.domain.model.RecipeMetadata
import com.example.brewbuddy.domain.recipe_similarity_calculator.RecipeSimilarityCalculator
import com.example.brewbuddy.domain.repository.RecipeRepository
import com.example.brewbuddy.profile.UserPreferences
import javax.inject.Inject

class RecommendationEngine @Inject constructor(private val recipeRepository: RecipeRepository){
    suspend fun getRecommendedRecipes(userPreferences: UserPreferences): List<RecipeMetadata> {
        val allRecipes = recipeRepository.getAllRecipes()

        val compatibleRecipes = allRecipes.filter { recipe ->
                    recipe.dairyFree == userPreferences.lactoseFree &&
                    recipe.vegan == userPreferences.vegan &&
                    recipe.vegetarian == userPreferences.vegetarian &&
                    recipe.glutenFree == userPreferences.glutenFree
        }

        val sortedRecipes = compatibleRecipes.sortedByDescending { recipe ->
            RecipeSimilarityCalculator.calculateCosineSimilarity(recipe, userPreferences)
        }
        Log.d("ALL_RECIPES", "$allRecipes")
        Log.d("COMPATIBLE RECIPES", "$compatibleRecipes")
        return sortedRecipes.map { it.toRecipeMetadata() }
    }
}