package com.example.brewbuddy.domain.use_case.get_content_based_recommendations

import com.example.brewbuddy.data.remote.dto.RecipeMetadataDto
import com.example.brewbuddy.domain.model.Recipe
import com.example.brewbuddy.domain.model.RecipeMetadata
import com.example.brewbuddy.domain.model.User
import com.example.brewbuddy.domain.recommendation_engine.RecommendationEngine
import com.example.brewbuddy.domain.repository.RecipeRepository
import com.example.brewbuddy.profile.UserPreferences
import javax.inject.Inject

class GetContentBasedRecommendations @Inject constructor(
    private val recommendationEngine: RecommendationEngine
) {
    suspend fun execute(userPreferences: UserPreferences): List<RecipeMetadata> {
        return recommendationEngine.getRecommendedRecipes(userPreferences)
    }
}