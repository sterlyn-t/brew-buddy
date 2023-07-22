package com.example.brewbuddy.data.repository
import android.util.Log
import com.example.brewbuddy.data.remote.dto.RecipeDto
import com.example.brewbuddy.data.remote.dto.RecipeMetadataDto
import com.example.brewbuddy.domain.repository.RecipeRepository
import com.example.brewbuddy.requests.getFunctions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeRepositoryImplementation @Inject constructor () : RecipeRepository {

    override suspend fun getRecipes(query: String?): List<RecipeMetadataDto> {
        Log.d("GET_RECIPES", "Running")

        return withContext(Dispatchers.IO) {
            val dataDeferred = async {
                if(query != null) {
                    getFunctions()
                        .getHttpsCallable("getRecipesMetadata")
                        .call(hashMapOf("query" to query)).await()

                } else {
                    getFunctions()
                        .getHttpsCallable("getRecipesMetadata")
                        .call().await()
                }
            }
            val task = dataDeferred.await()
            val data = task.data as List<HashMap<String, Object>>
            return@withContext data.map{RecipeMetadataDto.from(it)}
        }

    }
    override suspend fun getRecipeById(id: String): RecipeDto {
        Log.d("GET_RECIPE_BY_ID", id)

        return withContext(Dispatchers.IO) {
            val dataDeferred = async {
                getFunctions()
                    .getHttpsCallable("getRecipeById")
                    .call(hashMapOf("recipeId" to id)).await()
            }
            val task = dataDeferred.await()
            val data = task.data as HashMap<String, Object>
            return@withContext RecipeDto.from(data)
        }
    }
    override suspend fun getPopular(): List<RecipeMetadataDto> {
        Log.d("GET_POPULAR", "Running")

        return withContext(Dispatchers.IO) {
            val dataDeferred = async {
                getFunctions()
                    .getHttpsCallable("getPopularRecipes")
                    .call().await()
            }
            val task = dataDeferred.await()
            val data = task.data as List<HashMap<String, Object>>
            return@withContext data.map{RecipeMetadataDto.from(it)}
        }
    }

    override suspend fun getAllRecipes(): List<RecipeMetadataDto> {
        Log.d("GET_ALL_RECIPES", "Running")
        return withContext(Dispatchers.IO) {
            val dataDeferred = async {
                getFunctions()
                    .getHttpsCallable("getRecipesMetadata")
                    .call().await()
            }
            val task = dataDeferred.await()
            val data = task.data as List<HashMap<String, Object>>
            return@withContext data.map{RecipeMetadataDto.from(it)}
        }
    }
}