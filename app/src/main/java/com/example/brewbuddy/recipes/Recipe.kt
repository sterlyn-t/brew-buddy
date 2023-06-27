package com.example.brewbuddy.recipes;

import com.example.brewbuddy.R
import org.jetbrains.annotations.Nullable
import java.util.ArrayList;

public class Recipe {
    var recipeName: String = "";
    var description: String = "";
    var ingredientList: ArrayList<Ingredient> = ArrayList();
    var thumbnail: Int = R.drawable.default_recipe;

    constructor(name: String = "", desc: String = "", recipeList: ArrayList<Ingredient> = ArrayList()){
        this.recipeName = name;
        this.description = desc;
        this.ingredientList = recipeList;
    }

    fun getName(): String {
        return recipeName
    }

    fun getIngredients(): ArrayList<Ingredient> {
        return ingredientList;
    }

    fun getThumbNail(): Int {
        return thumbnail;
    }
}
