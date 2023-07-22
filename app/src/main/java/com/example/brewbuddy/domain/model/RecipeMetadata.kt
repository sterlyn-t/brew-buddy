package com.example.brewbuddy.domain.model

import com.example.brewbuddy.common.Constants

data class RecipeMetadata(
    val id: String,
    val title: String = "Unnamed Recipe",
    val bannerUrl: String = Constants.DEFAULT_BANNER_URL,
    val author: Author = Author(),
    var vegetarian: Boolean,
    var vegan: Boolean,
    var glutenFree: Boolean,
    var dairyFree: Boolean,
)