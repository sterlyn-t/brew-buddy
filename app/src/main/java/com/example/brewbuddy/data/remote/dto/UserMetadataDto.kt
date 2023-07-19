package com.example.brewbuddy.data.remote.dto

import com.example.brewbuddy.domain.model.UserMetadata

data class UserMetadataDto(
    val id: String,
    val likedRecipeIds: List<String>,
    val savedRecipeIds: List<String>,
) {
    companion object {
        fun from(map: HashMap<String, Object>) = object  {
            val data = UserMetadataDto(
                id=map["id"] as String,
                likedRecipeIds=map["likedRecipeIds"] as List<String>,
                savedRecipeIds=map["savedRecipeIds"] as List<String>,
            )
        }.data
    }
}

fun UserMetadataDto.toUserMetadata(): UserMetadata {
    return UserMetadata(
        id = id,
        likedRecipeIds = likedRecipeIds,
        savedRecipeIds = savedRecipeIds,
    )
}




