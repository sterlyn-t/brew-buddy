package com.example.brewbuddy.di

import com.example.brewbuddy.common.Constants
import com.example.brewbuddy.data.repository.RecipeRepositoryImplementation
import com.example.brewbuddy.domain.repository.RecipeRepository
import dagger.Provides
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesRecipeRepository(): RecipeRepository {
        return RecipeRepositoryImplementation()
    }
}