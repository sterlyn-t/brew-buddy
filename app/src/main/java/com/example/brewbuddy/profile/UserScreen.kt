package com.example.brewbuddy.profile

import androidx.annotation.DrawableRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.brewbuddy.CardWithIconAndTitle
import com.example.brewbuddy.ProfilePicture
import com.example.brewbuddy.R
import com.example.brewbuddy.getUser
import com.example.brewbuddy.ui.theme.GreyLight
import com.example.brewbuddy.ui.theme.GreyMedium
import com.example.brewbuddy.ui.theme.TitleLarge

private fun getIndex(currentIndex: Int, startIndex: Int, pageCount: Int): Int {
    val diff = currentIndex - startIndex;
    if (diff < 0) {
        return currentIndex % pageCount
    }
    return diff % pageCount
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Carousel() {
    val pageCount = 5
    val bounds = 100 // arbitrarily large # to give the illusion of infinite scroll
    val startIndex = bounds / 2

    val pagerState = rememberPagerState(
        initialPage = startIndex,
        initialPageOffsetFraction = 0f,
    ) {
        bounds
    }
    val focusColor = GreyMedium
    val unfocusedColor = GreyLight
    Column(modifier=Modifier.fillMaxWidth()) {

        HorizontalPager(
            modifier = Modifier,
            state = pagerState,
            pageSpacing = 16.dp,
            contentPadding = PaddingValues(horizontal = 100.dp),
            pageSize = PageSize.Fixed(200.dp),
            key = null,
            beyondBoundsPageCount = 1,
            pageContent = { index ->
                val page = getIndex(index, startIndex, pageCount)

                Box(contentAlignment = Alignment.Center) {
                    CardWithIconAndTitle(modifier = Modifier.width(200.dp).height(150.dp))

                }
            },
        )

        Row(
            modifier=Modifier.align(Alignment.CenterHorizontally).padding(top=10.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            repeat(pageCount) { index ->
                val color = if ( getIndex(pagerState.currentPage, startIndex, pageCount) == index) focusColor else unfocusedColor
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(12.dp)
                        .background(color)
                )
            }

        }

    }
}
@Composable
fun UserScreen(menuButton: @Composable () -> Unit) {
    val user = getUser()

    Column(modifier = Modifier.fillMaxSize()) {
        ProfileHeader(user, menuButton)
        Column() {
            TitleLarge(text="Pinned Recipes")
            Carousel()
        }
    }
}

@Composable
fun ProfileHeader(user: User, menuButton: @Composable () -> Unit) {
    val profilePictureSize = 126.dp
    val username = user.getUsername()
    val avatar = user.getAvatar()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(){
            ProfileBanner(R.drawable.profile_banner)
            menuButton()
        }
        Box(
            modifier= Modifier
                .offset(y = -(profilePictureSize / 2 + 25.dp))
                .fillMaxWidth()
        ) {
            Surface(
                modifier= Modifier
                    .offset(y = profilePictureSize / 2)
                    .height(profilePictureSize - 25.dp)
                    .fillMaxWidth(),
                shape= RoundedCornerShape(topEnd = 25.dp, topStart = 25.dp)
            ) {
                Text(
                    text=username,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.offset(y=profilePictureSize/2)
                )

            }
            Box(modifier=Modifier.align(Alignment.TopCenter)) {
                ProfilePicture(avatar, profilePictureSize)
            }
        }
    }
}
@Composable
fun ProfileBanner(@DrawableRes img: Int) {
    Box(modifier = Modifier.height(200.dp)) {
        Image(
            painter = painterResource(id = img),
            contentDescription = "Profile Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()

        )
    }
}