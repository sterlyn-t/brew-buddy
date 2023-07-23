package com.example.brewbuddy

import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.brewbuddy.domain.model.Author
import com.example.brewbuddy.marketplace.Filter
import com.example.brewbuddy.recipes.RecipeScreenViewModel
import com.example.brewbuddy.recipes.TagList
import com.example.brewbuddy.ui.theme.AuthorCardDisplay
import com.example.brewbuddy.ui.theme.Cream
import com.example.brewbuddy.ui.theme.GreenLight
import com.example.brewbuddy.ui.theme.GreenMedium
import com.example.brewbuddy.ui.theme.SlateLight

sealed class RecipeNavigationScreens(val route: String) {
    object IndividualRecipe : RecipeNavigationScreens("Recipes/{recipeId}")
}


@Composable
fun RecipesScreen (
    navController: NavHostController,
    viewModel: RecipeScreenViewModel = hiltViewModel()
) {

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchBarWrapper(viewModel)
            Surface(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
            ) {
                RecipeSearchResults(navController, viewModel)
            }
        }
    }

}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
    ExperimentalLayoutApi::class
)
@Composable
private fun SearchBar(viewModel: RecipeScreenViewModel) {
    var filtersExpanded by remember { mutableStateOf(false) }
    var searchQuery = viewModel.search.value
    Log.d("SEARCH BAR", "RECOMPOSE")
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { viewModel.search() }) {
                Icon(
                    painterResource(id = R.drawable.icon_search),
                    contentDescription = null
                )
            }
            TextField(
                value = searchQuery,
                onValueChange = {
                    viewModel.setKeywords(it);
                },
                label = { Text("Search") },
                colors = TextFieldDefaults
                    .textFieldColors(
                        containerColor = Color.White,
                        textColor = Color.DarkGray,
                        unfocusedIndicatorColor = Color.LightGray,
                        focusedIndicatorColor = Color.Gray,
                        disabledLeadingIconColor = Color.DarkGray,
                        disabledIndicatorColor = Color.DarkGray
                    ),

                modifier = Modifier
                    .width(302.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    /*onSearch(text)*/
                    // Hide the keyboard after submitting the search
                    keyboardController?.hide()
                    //or hide keyboard
                    focusManager.clearFocus()

                })
            )
            Row(
                modifier = Modifier.padding(top = 4.dp, end = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                Box(
                    modifier = Modifier
                        .height(52.dp)
                        .padding(top = 8.dp)
                        .padding(top = 8.dp, start = 2.dp, end = 8.dp)

                        .align(Alignment.CenterVertically)
                ) {
                    IconButton(onClick = { filtersExpanded = !filtersExpanded }) {
                        BadgedBox(
                            badge = {
                                Badge(containerColor = GreenMedium) {
                                    Text(
                                        text = viewModel.filters.size.toString(),
                                        color = Color.White
                                    )
                                }
                            }
                        ) {
                            Icon(
                                painterResource(id = R.drawable.icon_tune),
                                contentDescription = null,
                                modifier = Modifier.size(28.dp),
                                tint = Color.Gray,
                            )
                        }
                    }
                }
                RecipeFilters(
                    state=filtersExpanded,
                    onDismissRequest = { filtersExpanded = false },
                    viewModel = viewModel
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(start = 12.dp, end = 12.dp)
        ) {
            for (filter in viewModel.filters) {
                FilterTag(filter = filter, viewModel = viewModel)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun RecipeFilters(state: Boolean, onDismissRequest: () -> Unit, viewModel: RecipeScreenViewModel) {
    DropdownMenu(
        expanded = state,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier=Modifier.width(200.dp)) {
                Label("Filters")
                TagList.forEach { tagInfo ->
                    val filter = Filter(name=tagInfo.name, filterLabel = tagInfo.label, enabled=true)
                    DropdownMenuItem(
                        leadingIcon = {if(viewModel.filters.contains(filter)) {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_checked_box),
                                contentDescription = "Checked",
                                modifier = Modifier.size(24.dp),
                                tint = Color.Black
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.icon_box),
                                contentDescription = "Checked",
                                modifier = Modifier.size(24.dp),
                                tint = Color.Black
                            )
                        }},
                        text = { Text(tagInfo.label) },
                        onClick = {
                            updateActiveFilters(filter, viewModel)
                            viewModel.search()
                        }
                    )
                }
            }
            Column() {
                Label("Sort by")
                SortFilters.forEach { filter ->
                    DropdownMenuItem(
                        text = { Text(filter.filterLabel) },
                        onClick = {
                            updateActiveFilters(filter, viewModel)
                            viewModel.search()
                        }
                    )
                }
            }
        }
    }

}

@Composable
private fun Label(text: String) {
    Text(text=text, modifier=Modifier.padding(start=12.dp))
}
private fun updateActiveFilters(
    filterToAdd: Filter,
    viewModel: RecipeScreenViewModel
) {

    val likesLowToHigh = SortFilters.last { filter: Filter -> filter.name == "likesAsce"}
    val likesHighToLow = SortFilters.last { filter: Filter -> filter.name == "likesDesc" }
    if (viewModel.filters.contains(filterToAdd)) {
        viewModel.removeFilter(filterToAdd)
        return
    }

    if (filterToAdd.name == "likesAsce"
        && viewModel.filters.contains(likesHighToLow))
    {
        viewModel.removeFilter(likesHighToLow)
        viewModel.addFilter(filterToAdd)

        return
    }
    if (filterToAdd.name == "likesDesc"
        && viewModel.filters.contains(likesLowToHigh))
    {
        viewModel.removeFilter(likesLowToHigh)
        viewModel.addFilter(filterToAdd)

        return
    }
    viewModel.addFilter(filterToAdd)
    return
}

@Composable
private fun FilterTag(filter: Filter, viewModel: RecipeScreenViewModel) {
    Box(modifier = Modifier.padding(top = 6.dp)) {
        Box(
            modifier =
            Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(color = GreenLight)
                .height(28.dp)
            ,
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp)
            ) {
                Box(modifier = Modifier.align(Alignment.CenterVertically)) {
                    IconButton(
                        onClick = {
                            viewModel.filters.remove(filter);
                            viewModel.search()
                        },
                        modifier = Modifier.size(16.dp)) {
                        Canvas(modifier = Modifier.size(16.dp)) {
                            drawCircle(color = SlateLight)
                        }
                        Icon(
                            tint = Color.Black,
                            painter = painterResource(id = R.drawable.icon_close),
                            contentDescription = "Display icon",
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = filter.filterLabel,
                    color = Color.Black,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun SearchBarWrapper(viewModel: RecipeScreenViewModel) {
    Box(modifier = Modifier
        .background(color = Color.White)) {
        SearchBar(viewModel)
    }
}

@Composable
private fun RecipeSearchResults(navController: NavHostController, viewModel: RecipeScreenViewModel) {
    val state = viewModel.state.value
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
            Box(modifier=Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier
                    .align(Alignment.Center)
                    .size(34.dp))
            }
        }
    } else {
        Surface(
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = Color.Transparent)
        )
        {

            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 34.dp, bottom = 94.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                for (el in state.results) {
                    ResultCard(
                        title = el.title,
                        bannerUrl = el.bannerUrl,
                        recipeId = el.id,
                        author = el.author,
                        navController = navController
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ResultCard(
    title: String,
    bannerUrl: String,
    recipeId: String,
    author: Author,
    navController: NavHostController
) {
    Card(
        modifier = Modifier
            .height(160.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        onClick = { navigateToRecipe(recipeId, navController) }
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(modifier = Modifier.width(135.dp)) {
                AsyncImage(
                    model = bannerUrl,
                    contentDescription = "Recipe Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Row() {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(28.dp),
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(bottom = 8.dp)
                ) {

                    Column(horizontalAlignment = Alignment.End) {
                        AuthorCardDisplay(author, textColor=Color.Black)
                    }
                }
            }
        }
    }
}
//


private val SortFilters = listOf(
    Filter("likesAsce", "Popularity (Low to High)", false),
    Filter("likesDesc", "Popularity (High to Low)", false)
)
