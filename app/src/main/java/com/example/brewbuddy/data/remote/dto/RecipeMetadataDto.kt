package com.example.brewbuddy.data.remote.dto

import com.example.brewbuddy.domain.model.Author
import com.example.brewbuddy.domain.model.Recipe
import com.example.brewbuddy.domain.model.RecipeMetadata

data class RecipeMetadataDto(
    val id: String,
    val title: String,
    val bannerUrl: String,
    val author: Author,
    val vegan: Boolean,
    val vegetarian: Boolean,
    val glutenFree: Boolean,
    val dairyFree: Boolean
) {
    companion object {
        fun from(map: HashMap<String, Object>) = object {
            val data = RecipeMetadataDto(
                id=map["id"] as String,
                title=map["title"] as String,
                bannerUrl=map["bannerUrl"] as String,
                author=Author.from(map["author"] as HashMap<String, Object>),
                glutenFree=map["glutenFree"] as Boolean,
                dairyFree=map["dairyFree"] as Boolean,
                vegetarian=map["vegetarian"] as Boolean,
                vegan=map["vegan"] as Boolean,
            )
        }.data
    }
}

fun RecipeMetadataDto.toRecipeMetadata(): RecipeMetadata {
    return RecipeMetadata(
        id = id,
        bannerUrl = bannerUrl,
        title = title,
        author=author,
        vegan=vegan,
        vegetarian=vegetarian,
        glutenFree=glutenFree,
        dairyFree=dairyFree,
    )
}