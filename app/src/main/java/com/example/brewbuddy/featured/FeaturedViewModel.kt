package com.example.brewbuddy.featured

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewbuddy.common.Resource
import com.example.brewbuddy.data.remote.dto.RecipeMetadataDto
import com.example.brewbuddy.domain.model.RecipeMetadata
import com.example.brewbuddy.domain.use_case.get_content_based_recommendations.GetContentBasedRecommendations
import com.example.brewbuddy.domain.use_case.get_recipes.GetPopularUseCase
import com.example.brewbuddy.domain.use_case.get_recipes.GetRecipesUseCase
import com.example.brewbuddy.featured.FeaturedState
import com.example.brewbuddy.profile.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeaturedViewModel @Inject constructor(
    private val getRecipesUseCase: GetRecipesUseCase,
    private val getPopularUseCase: GetPopularUseCase,
    private val getContentBasedRecommendations: GetContentBasedRecommendations
): ViewModel() {

    private val _recipeState = mutableStateOf(FeaturedState())
    private val _popularState = mutableStateOf(FeaturedState())

    val recipeState: State<FeaturedState> = _recipeState
    val popularState: State<FeaturedState> = _popularState

    private val _recommendedRecipes = mutableStateOf(FeaturedState())
    val recommendedRecipes: State<FeaturedState> = _recommendedRecipes

    private val userPreferences = (UserPreferences(50f, vegan = true, vegetarian = true, lactoseFree = false,
        kosher = false,
        halal = true,
        glutenFree = true,
        nutFree = false
    ))
    private fun fetchRecommendedRecipes(userPreferences: UserPreferences) {
 /*   userPreferences.observeForever { preferences ->*/
        viewModelScope.launch {
            val recipes = getContentBasedRecommendations.execute(userPreferences)
            _recommendedRecipes.value = FeaturedState(data = recipes)
        }
/*    }*/
}

    init {
        getPopular()
        getRecipes()
        fetchRecommendedRecipes(userPreferences)
    }

    private fun getPopular() {
        getPopularUseCase().onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _popularState.value = FeaturedState(data = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _popularState.value = FeaturedState(error = result.message ?: "An unexpected error occurred.")
                }
                is Resource.Loading -> {
                    _popularState.value = FeaturedState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getRecipes() {
        getRecipesUseCase().onEach { result ->
            when(result) {
                is Resource.Success -> {
                    _recipeState.value = FeaturedState(data = result.data ?: emptyList())
                }
                is Resource.Error -> {
                    _recipeState.value = FeaturedState(error = result.message ?: "An unexpected error occurred.")
                }
                is Resource.Loading -> {
                    _recipeState.value = FeaturedState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}
