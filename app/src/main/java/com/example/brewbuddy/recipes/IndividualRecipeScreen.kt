package com.example.brewbuddy.recipes

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.brewbuddy.R
import com.example.brewbuddy.data.remote.dto.IngredientList
import com.example.brewbuddy.domain.model.Author
import com.example.brewbuddy.domain.model.Recipe
import com.example.brewbuddy.domain.use_case.analytics_manager.LocalAnalytics
import com.example.brewbuddy.ui.theme.Brown
import com.example.brewbuddy.ui.theme.Cream
import com.example.brewbuddy.ui.theme.GreenLight
import com.example.brewbuddy.ui.theme.TitleLarge
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase


sealed class RecipeNavigationScreens(val route: String) {
    object IndividualRecipe : RecipeNavigationScreens("Recipes/{recipeId}")
}
private val firebaseAnalytics = Firebase.analytics

@Composable
fun IndividualRecipeScreen(
    navController: NavHostController,
    viewModel: IndividualRecipeScreenViewModel = hiltViewModel()
) {
    var state = viewModel.state.value


    if(state.error.isNotBlank()) {
        Text(
            text = state.error,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
    } else if(state.isLoading){
        Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
            Box() {
                CircularProgressIndicator(modifier = Modifier
                    .align(Alignment.Center)
                    .size(34.dp))
            }
        }
    } else {
        Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box() {
                    RecipeBanner(
                        state.recipe!!.id!!,
                        state.recipe!!.bannerUrl!!,
                        state.recipe!!.title!!,
                        state.recipe!!.author!!,
                        navController,
                    )
                }
                Box(
                    modifier = Modifier
                        .offset(y = -(20.dp))
                        .padding(bottom = 60.dp)
                        .fillMaxSize()
                ) {
                    RecipeSection(state.recipe)
                }
            }
        }

    }
}

private fun logAnalyticsEvent(id: String) {
    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
        param(FirebaseAnalytics.Param.ITEM_ID, id)
    }
}
@Composable
private fun RecipeBanner(
    id: String,
    img: String,
    title: String,
    author: Author,
    navController: NavHostController,
) {
    val contextForToast = LocalContext.current.applicationContext
    val analyticsManager = LocalAnalytics.current
    var params: Bundle =  Bundle()
    params.putString("RECIPE_ID", id)

    Box(modifier = Modifier
        .height(230.dp)
        .fillMaxWidth(),
    ) {
        AsyncImage(
            model = img,
            contentDescription = "Recipe Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp)) {
            IconButton(onClick = { if (navController.previousBackStackEntry != null) navController.popBackStack() else null}) {
                Icon(
                    tint = Cream,
                    painter = painterResource(id = R.drawable.icon_expand_circle_down),
                    contentDescription = "Navigate back",
                    modifier = Modifier
                        .background(Color.Transparent)
                        .size(60.dp)
                        .rotate(90.0F)
                )
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween)
        {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 24.dp)) {
                Row() {
                    Text(style = MaterialTheme.typography.titleLarge, text =  title, color =  Cream)
                }
                Row() {
                    Text(modifier = Modifier.padding(horizontal = 2.dp), text = "@" + author.username, color = Cream)
                }
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 2.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Box( modifier = Modifier.align(Alignment.CenterVertically)) {
                        IconButton(
                            onClick =
                            {
                                analyticsManager?.logEventWithParams("added_to_wishlist", params)
                                Toast.makeText(contextForToast, "Added to Favourites", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Canvas(modifier = Modifier.size(38.dp)) {
                                drawCircle(color = Cream)
                            }
                            Icon(
                                tint = Brown,
                                painter = painterResource(id = R.drawable.icon_favourite_heart),
                                contentDescription = "Favourite Recipe",
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            tint = Cream,
                            painter = painterResource(id = R.drawable.icon_more_vertical),
                            contentDescription = "Explore more",
                            modifier = Modifier.size(42.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecipeSection(recipe: Recipe?) {
    if(recipe == null) {
        Surface(
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = Color.Transparent)
        ) {
            TitleLarge(text = "Recipe could not be found.")
        }
    } else {
        var recipeRating = if (recipe.likes >= 5) 5.0 else recipe.likes.toDouble()
        Surface(
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = Color.Transparent)
        )
        {
            Column(horizontalAlignment = Alignment.Start,
            ) {
                Row(modifier = Modifier
                    .padding(vertical = 14.dp, horizontal = 4.dp)
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        LabelledIcon(
                            img = R.drawable.icon_time,
                            label = recipe.preparationMinutes.toString() + " min"
                        )
                        Divider(modifier = Modifier
                            .width(1.dp)
                            .size(28.dp), color = Brown)
                        LabelledIcon(
                            img = R.drawable.icon_mug,
                            label = "Serves " + recipe.servings.toString()
                        )
                        Divider(modifier = Modifier
                            .width(1.dp)
                            .size(28.dp), color = Brown)
                        LabelledIcon(
                            img = R.drawable.icon_star_outline,
                            label = recipeRating.toString()
                        )
                    }
                }
                Row(modifier = Modifier.padding(top = 8.dp)) {
                  TagsSection(recipe)
                  }
                Row(modifier = Modifier.padding(top = 16.dp)) {
                    IngredientsSection(ingredients = recipe.ingredientLists)
                }
                Row(modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxSize()
                ) {
                    PreparationSection(recipe)
                }
            }
        }
    }

}

@Composable
private fun LabelledIcon(@DrawableRes img: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            tint = Brown,
            painter = painterResource(id = img),
            contentDescription = "Display icon",
            modifier = Modifier.size(28.dp)
        )
        Text(text = label, fontSize = 12.sp)
    }
}

private fun generateTags(recipe: Recipe?): List<TagType> {
    var recipeTags = mutableListOf<TagType>()
    var isDairyFree = recipe?.dairyFree
    var isGlutenFree = recipe?.glutenFree
    var isVegan = recipe?.vegan
    var isVegetarian = recipe?.vegetarian

    if (!isDairyFree!!) {
        recipeTags.add(
            TagType(
                tagColor = GreenLight,
                iconTint = Color.White,
                tagText = "Dairy",
                img = R.drawable.icon_info
            )
        )
    }

    if (isGlutenFree!!) {
        recipeTags.add(
            TagType(
                tagColor = GreenLight,
                iconTint = Color.White,
                tagText = "Gluten Free",
                img = R.drawable.icon_custom_gluten_free
            )
        )
    }

    if (isVegan!!) {
        recipeTags.add(
            TagType(
                tagColor = Brown,
                iconTint = Color.White,
                tagText = "Vegan",
                img = R.drawable.icon_custom_vegan
            )
        )
    }

    if (isVegetarian!!) {
        recipeTags.add(
            TagType(
                tagColor = Brown,
                iconTint = Color.White,
                tagText = "Vegetarian",
                img = R.drawable.icon_custom_vegetarian
            )
        )
    }
    return recipeTags
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TagsSection(
    recipe: Recipe?
) {
    var tags = generateTags(recipe)
    FlowRow(
        modifier = Modifier
            .padding(start = 22.dp)
            .background(color = Color.Transparent),
        maxItemsInEachRow = 4,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
       tags.forEach {
            Tag(it.iconTint, it.tagColor, it.tagText, it.img)
        }
    }
}

@Composable
private fun IngredientsSection(ingredients: List<IngredientList>) {
    Column(modifier = Modifier.padding(start = 24.dp)) {
        Row(modifier = Modifier.padding(bottom = 4.dp)) {
            Text("Ingredients", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        Column() {
            ingredients.forEach {
                Row() {
                    SectionHeading(it.name, preparationHeading = false)
                }
                for (i in 0 until it.ingredients.size){
                    val ingredient = it.ingredients[i]
                    Row(){
                        IngredientBullet(
                            quantity = ingredient.quantity.imperial.amount,
                            unit = ingredient.quantity.imperial.unitShort,
                            subIngredientDetail = ingredient.name
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeading(heading: String, preparationHeading: Boolean) {
    val headingString = if (preparationHeading) "For the $heading" else heading
    Text(
        modifier = Modifier
            .padding(top = 4.dp, bottom = 4.dp),
        fontStyle = FontStyle.Italic,
        text = heading,
        style = MaterialTheme.typography.bodyMedium
        )
}

@Composable
private fun IngredientBullet(quantity: Number, unit: String, subIngredientDetail: String) {
    var displayQuantity = quantity.toString()
    if (
        displayQuantity.contains('.') &&
        displayQuantity.substringAfter('.').length > 2
    ) {
        displayQuantity = displayQuantity.substring(0, displayQuantity.indexOf(".") + 2)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 10.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .width(70.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Canvas(modifier = Modifier.size(6.dp),) {
                    drawCircle(SolidColor(Color.Black))
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "$displayQuantity ${unit.lowercase() ?: ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Column(modifier = Modifier
            .align(Alignment.CenterVertically)
            .padding(start = 4.dp)
        ) {
            Text(text = subIngredientDetail, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun PreparationSection(recipe: Recipe) {
    Column(modifier = Modifier
        .padding(start = 24.dp, end = 24.dp)
        .fillMaxHeight()) {
        Row(modifier = Modifier.padding(bottom = 4.dp)) {
            Text("Preparation", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
        Column(modifier = Modifier.fillMaxSize()) {
            recipe.instructions.forEach {
                Row() {
                    SectionHeading(it.name, preparationHeading = true)
                }
                for (i in 0 until it.steps.size) {
                    val stepNumber = (i + 1).toString()
                    Row(modifier = Modifier.padding(end = 8.dp, bottom = 6.dp)) {
                        Text(
                            modifier = Modifier.padding(start = 10.dp),
                            text = it.steps[i].number.toString() + ". " + it.steps[i].step,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Tag(iconTint: Color,
                tagColor: Color,
                tagText: String,
                @DrawableRes img: Int
) {
    Box(modifier = Modifier.padding(top = 6.dp)) {
        Box(
            modifier =
            Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(color = tagColor),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
            ) {
                Icon(
                    tint = iconTint,
                    painter = painterResource(id = img),
                    contentDescription = "Display icon",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = tagText,
                    color = Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}