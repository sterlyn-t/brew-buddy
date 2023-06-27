package com.example.brewbuddy

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.MaterialTheme
import com.example.brewbuddy.util.randomSampleImageUrl
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.runtime.MutableState
import com.example.brewbuddy.ui.theme.GreyLight
import com.example.brewbuddy.ui.theme.GreyMedium
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.brewbuddy.ui.theme.Cream
import com.example.brewbuddy.randomSizedPhotos as randomSizedPhotos

@Composable
fun RecipesScreen(
    name: String
) {
    val openRecipeModal = remember { mutableStateOf(false)}
    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Column(
            modifier = Modifier
                .padding(
                    start = 0.dp,
                    top = 16.dp,
                    bottom = 0.dp,
                    end = 0.dp
                )
        ) {
            RecipeGridLayout()
        }
    }
}
@Composable
private fun Heading(text: String) {
    Box(
        modifier = Modifier
            .padding(
                start = 8.dp,
                top = 0.dp,
                bottom = 0.dp,
                end = 0.dp
            )
            .fillMaxWidth()
    ) {
        Text(text,
            fontWeight = FontWeight.Bold,
            fontSize=22.sp
        )
    }
}

@Composable
private fun CardTitle(text: String, fontSize: TextUnit) {
    Text(
        text,
        color = Color.White,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RecipeGridLayout() {
     LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 14.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .height(500.dp)
        ) {

            item( span = StaggeredGridItemSpan.FullLine) {
                Heading(text = "Popular")
            }
            item(
                span = StaggeredGridItemSpan.FullLine
            ) {
                Carousel(
                    itemsCount = shorterList.size,
                    itemContent = {  index ->
                        PopularCard(photo = randomSizedPhotos[index])
                    })
            }
            item(
              span = StaggeredGridItemSpan.FullLine
             ) {
             Heading(text = "Picked for you")
         }
            items(randomSizedPhotos) { photo ->
                RecipeCard(photo = photo)
            }
     }
 }


@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun PopularCard(photo: String) {
    Card(
        onClick = { },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp
        ),
    ) {
        Box() {
            AsyncImage(
                model = photo,
                contentDescription = null,
                contentScale = ContentScale.Crop,
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
                            text = "Card Title",
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecipeCard(photo: String) {
    Card(
        onClick = {},
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp
        ),
        modifier = Modifier.fillMaxWidth()

    ) {
            Box() {
                AsyncImage(
                    model = photo,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .fillMaxHeight()
                )
            Row() {
                Box(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.icon_page),
                        contentDescription = "Location Pin Icon",
                        modifier = Modifier.size(36.dp),
                        tint = Color.White
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding
                        (
                        start = 0.dp,
                        top = 130.dp,
                        end = 0.dp,
                        bottom = 0.dp
                    )
            )
            {
                Box() {
                    Box(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
                        CardTitle(
                            "Card Title Testing Longer Ones",
                            fontSize = 24.sp
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding
                                (
                                start = 0.dp,
                                top = 120.dp,
                                end = 8.dp,
                                bottom = 8.dp
                            )
                            .align(Alignment.CenterEnd)
                    ) {
                        Text("John Doe", color = Color.White, fontSize = 18.sp)
                        Box(
                            Modifier
                                .padding(horizontal = 6.dp, vertical = 0.dp)
                                .size(30.dp), contentAlignment = Alignment.Center) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawCircle(SolidColor(Color.White))
                            }
                            Icon(
                                painter = painterResource(id = R.drawable.icon_user),
                                contentDescription = "User image placeholder"
                            )
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Carousel(
    modifier: Modifier = Modifier,
    pagerState: PagerState = remember{ PagerState() },
    itemsCount: Int,
    itemContent: @Composable (index: Int) -> Unit,
) {
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        HorizontalPager(pageCount = itemsCount, state = pagerState, pageSize = PageSize.Fixed(300.dp), pageSpacing = 8.dp) { page ->
            itemContent(page)
        }
    }
    Box(modifier = modifier
        .fillMaxWidth()
        .padding(top = 2.dp, start = 0.dp, end = 0.dp, bottom = 12.dp)
        .offset(x = 0.dp, y = 200.dp)
    )
    {
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter),
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



private val randomSizedPhotos = listOf(
    randomSampleImageUrl(width = 1100, height = 900),
    randomSampleImageUrl(width = 900, height = 1600),
    randomSampleImageUrl(width = 500, height = 800),
    randomSampleImageUrl(width = 500, height = 700),
    randomSampleImageUrl(width = 1100, height = 900),
    randomSampleImageUrl(width = 500, height = 800),
    randomSampleImageUrl(width = 1100, height = 900),
    randomSampleImageUrl(width = 900, height = 1600),
    randomSampleImageUrl(width = 500, height = 700),
    randomSampleImageUrl(width = 500, height = 800),
    randomSampleImageUrl(width = 1100, height = 900),
    randomSampleImageUrl(width = 500, height = 700),
    randomSampleImageUrl(width = 900, height = 1600),
    randomSampleImageUrl(width = 500, height = 800),
    randomSampleImageUrl(width = 500, height = 700),
    randomSampleImageUrl(width = 1100, height = 900),
    randomSampleImageUrl(width = 500, height = 800),
    randomSampleImageUrl(width = 500, height = 700),
    randomSampleImageUrl(width = 500, height = 800),
    randomSampleImageUrl(width = 1100, height = 900),
    randomSampleImageUrl(width = 500, height = 700),
    randomSampleImageUrl(width = 900, height = 1600),
    randomSampleImageUrl(width = 500, height = 800),
    randomSampleImageUrl(width = 500, height = 700),
    randomSampleImageUrl(width = 1100, height = 900),
    randomSampleImageUrl(width = 500, height = 800),
)
private val shorterList = randomSizedPhotos.subList(0, 6)
