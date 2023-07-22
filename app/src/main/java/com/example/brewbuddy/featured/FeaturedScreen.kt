package com.example.brewbuddy

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.ComposableOpenTarget
import com.example.brewbuddy.ui.theme.GreyLight
import com.example.brewbuddy.ui.theme.GreyMedium
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.brewbuddy.domain.model.RecipeMetadata
import com.example.brewbuddy.recipes.RecipeNavigationScreens
import com.example.brewbuddy.featured.FeaturedViewModel
import com.example.brewbuddy.featured.FeaturedState
import com.example.brewbuddy.ui.theme.Cream
import com.example.brewbuddy.ui.theme.TitleLarge

@Composable
fun FeaturedScreen(
    navController: NavHostController,
    viewModel: FeaturedViewModel = hiltViewModel()
) {

    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier
                .padding(
                    start = 0.dp,
                    top = 16.dp,
                    bottom = 0.dp,
                    end = 0.dp
                )
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        ) {
            PopularRecipes(navController = navController, viewModel = viewModel)
            GridLayout(navController = navController, viewModel = viewModel)
        }
    }
}

@Composable
private fun Loading() {
    Box() {
        CircularProgressIndicator(modifier = Modifier
            .align(Alignment.Center)
            .size(34.dp))
    }
}

@Composable
private fun CardTitle(text: String, fontSize: TextUnit) {
    Text(
        text,
        color = Color.White,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold,
        overflow = TextOverflow.Ellipsis,
        modifier=Modifier.fillMaxWidth().wrapContentHeight()
    )
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PopularRecipes(viewModel: FeaturedViewModel, navController: NavHostController) {
    val state = viewModel.popularState.value
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(1),
        verticalItemSpacing = 14.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
        modifier = Modifier
            .height(300.dp)
    ) {
        item {
            TitleLarge(text = "Popular")
        }
        item {
            if(state.error.isNotBlank()) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )
            }
            if(state.isLoading){
                Surface(modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)) {
                    Loading()
                }
            }
        }
        item {
            Carousel(
                itemsCount = state.data.size,
                itemContent = { index ->
                    PopularCard(recipe = state.data[index], navController)
                }
            )
        }
    }

}
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun GridLayout(navController: NavHostController, viewModel: FeaturedViewModel) {
    val state = viewModel.recipeState.value
    val test = viewModel.recommendedRecipes.value

    val height = ((state.data.size*200) + 70).dp
    Column() {
        TitleLarge(text = "Picked for you")
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 14.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
            modifier = Modifier
                .fillMaxSize()
                .height(height)
        ) {
            items(test.data) {
                    recipe ->
                RecipeCard(recipe, navController)
            }
        }
    }

    if(state.error.isNotBlank()) {
        Text(
            text = state.error,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )
    }
    if(state.isLoading){
        Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
            Box() {
                CircularProgressIndicator(modifier = Modifier
                    .align(Alignment.Center)
                    .size(34.dp))
            }
        }
    }

}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PopularCard(recipe: RecipeMetadata, navController: NavHostController) {
    Card(
        onClick = {navigateToRecipe(recipe.id!!, navController)},
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp
        ),
        modifier = Modifier.height(190.dp)
            .width(260.dp)

    ) {
        Box() {
            AsyncImage(
                model = recipe.bannerUrl,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .height(190.dp)
                    .width(260.dp)
            )
            Box(modifier = Modifier.padding(start = 0.dp, top = 135.dp, bottom = 0.dp, end = 0.dp)) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.width(250.dp)
                ) {
                    Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                        CardTitle(
                            text = recipe.title,
                            fontSize = 22.sp
                        )
                    }
                    Box(modifier = Modifier.padding(start = 8.dp, end = 4.dp, top = 4.dp, bottom = 8.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_locator),
                            contentDescription = "Location Pin Icon",
                            modifier = Modifier.size(36.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

private fun navigateToRecipe(recipeId: String, navController: NavHostController) {
    navController.navigate(route = RecipeNavigationScreens.IndividualRecipe.route + recipeId)
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecipeCard(recipe: RecipeMetadata, navController: NavHostController) {
    Card(
        onClick = {navigateToRecipe(recipe.id!!, navController)},
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp
        ),
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)

    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = recipe.bannerUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.icon_page),
                    contentDescription = "Location Pin Icon",
                    modifier = Modifier.size(32.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.weight(1f).padding(bottom = 40.dp))
                CardTitle(text=recipe.title, fontSize = 24.sp)
                Row(
                    modifier=Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(recipe.author!!.username, color = Color.White, fontSize = 18.sp)
                    AsyncImage(
                        model = recipe.author!!.avatarUrl,
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(30.dp)
                            .border(4.dp, Color.White, shape = CircleShape)
                    )

//                    Box(
//                        Modifier
//                            .padding(horizontal = 6.dp, vertical = 0.dp)
//                            .size(30.dp), contentAlignment = Alignment.Center) {
//                        Canvas(modifier = Modifier.fillMaxSize()) {
//                            drawCircle(SolidColor(Color.White))
//                        }
//                        Icon(
//                            painter = painterResource(id = R.drawable.icon_user),
//                            contentDescription = "User image placeholder"
//                        )
//                    }
                }

            }
        }
    }
}



@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Carousel(
    pagerState: PagerState = remember{ PagerState() },
    itemsCount: Int,
    itemContent: @Composable (index: Int) -> Unit,
) {
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    Column(    verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        HorizontalPager(pageCount = itemsCount, state = pagerState, pageSize = PageSize.Fixed(300.dp), pageSpacing = 8.dp) { page ->
            itemContent(page)
        }
        Surface(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            shape = CircleShape,
            color = Color.Transparent
        ) {
            DotsIndicator(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                totalDots = itemsCount,
                selectedIndex = if (isDragged) pagerState.currentPage else pagerState.targetPage,
                dotSize = 12.dp
            )
        }
    }
}

@Composable
fun IndicatorDot(
    modifier: Modifier = Modifier,
    size: Dp,
    color: Color
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
    )
}
@Composable
fun DotsIndicator(
    modifier: Modifier = Modifier,
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color = GreyMedium,
    unSelectedColor: Color = GreyLight,
    dotSize: Dp
) {
    LazyRow(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .height(14.dp)
    ) {
        items(totalDots) { index ->
            IndicatorDot(
                color = if (index == selectedIndex) selectedColor else unSelectedColor,
                size = dotSize
            )

            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}
