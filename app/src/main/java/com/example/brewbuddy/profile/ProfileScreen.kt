package com.example.brewbuddy

import android.graphics.Paint.Align
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*


import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.brewbuddy.profile.CurrentUserViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.brewbuddy.profile.SettingScreen
import com.example.brewbuddy.profile.User
import com.example.brewbuddy.profile.UserScreen
import com.example.brewbuddy.ui.theme.OrangeBrownMedium
import com.example.brewbuddy.ui.theme.TitleLarge
import com.example.brewbuddy.ui.theme.currentRoute
import kotlinx.coroutines.CoroutineScope

sealed class ProfileScreens(val route: String, val label: String) {
    object User : ProfileScreens("profile/user", "Profile")
    object PinnedRecipes : ProfileScreens("profile/pinned_recipes", "Pinned Recipes")
    object Settings : ProfileScreens("profile/settings", "Settings")
}

@Composable
fun getUser(): User {
    val currentUserViewModel: CurrentUserViewModel = viewModel(viewModelStoreOwner = LocalNavGraphViewModelStoreOwner.current)
    return currentUserViewModel.getUser()
}
@Composable
fun CardWithIconAndTitle(modifier: Modifier) {
    Card(modifier) {
        val user = getUser()
        val username = user.getUsername()
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Filled.Face, contentDescription = "Localized description")
        }
        Row(modifier = Modifier.padding(16.dp)) {
            Text(
                text = username,
//                style = InterFont,
            )
        }
    }
}

@Composable
fun ProfilePicture(@DrawableRes img: Int, size: Dp) {
    Image(
        painter = painterResource(id = img),
        contentDescription = "Profile Picture",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .clip(CircleShape)
            .size(size)
            .border(4.dp, Color.White, shape = CircleShape)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    val coroutineScope = rememberCoroutineScope()
    var menuDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val localNavController = rememberNavController()
    val vmStoreOwner = rememberViewModelStoreOwner()
    val menuItems = listOf(
        ProfileScreens.User,
        ProfileScreens.Settings
    )
    ModalNavigationDrawer(
        drawerContent = {
            ProfileMenu(
                localNavController,
                menuItems,
                coroutineScope,
                menuDrawerState)
        },
        drawerState = menuDrawerState
    ) {
        Surface() {

            CompositionLocalProvider(
                LocalNavGraphViewModelStoreOwner provides vmStoreOwner
            ) {
                NavHost(localNavController, startDestination = ProfileScreens.User.route) {
                    composable(ProfileScreens.User.route) {
                        UserScreen(menuButton = {MenuButton(coroutineScope, menuDrawerState, Color.White)})
                    }

                    composable(ProfileScreens.Settings.route) {
                        SettingScreen(menuButton = {MenuButton(coroutineScope, menuDrawerState)})
                    }

                }

            }


        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
private fun toggleMenu(menuDrawerState: DrawerState, coroutineScope: CoroutineScope) {
    coroutineScope.launch {
        if(menuDrawerState.isClosed) menuDrawerState.open() else menuDrawerState.close()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MenuButton(coroutineScope: CoroutineScope, menuDrawerState: DrawerState, color: Color = Color.Black) {
    IconButton(
        onClick = { toggleMenu(menuDrawerState, coroutineScope) },
        modifier = Modifier.padding(6.dp)
    ) {
        Icon(
            painter = painterResource(id=R.drawable.icon_menu),
            contentDescription ="Profile Menu",
            tint= color
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileMenu(
    navController: NavHostController,
    items: List<ProfileScreens>,
    coroutineScope: CoroutineScope,
    menuDrawerState: DrawerState
){
    val user = getUser()
    val username = user.getUsername()

    ModalDrawerSheet(modifier= Modifier
        .width(280.dp)
        .padding(0.dp)
        .fillMaxHeight(),
        drawerShape = RectangleShape,
        drawerContainerColor = Color.White
    ) {
        val selectedItem = remember { mutableStateOf(items[0].route) }
        var selectedColor = MaterialTheme.colorScheme.primary
        val borderWidth = LocalDensity.current.run { 6.dp.toPx() }

        Button(
            onClick = {
                coroutineScope.launch { menuDrawerState.close() }
                navController.navigate(items[0].route)
                selectedItem.value = items[0].route
            },
            modifier = Modifier.fillMaxWidth(),
            colors=ButtonDefaults.buttonColors(
                containerColor = Color.Transparent, contentColor = Color.Black
            ),
            shape = RectangleShape
        ) {
            Row(
                modifier = Modifier.padding(top=20.dp, bottom=20.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                ProfilePicture(user!!.getAvatar(), 64.dp)
                Text(
                    text=username,
                    style=MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 10.dp))
            }

        }
        items.forEach { item ->
            NavigationDrawerItem(
                label = { Text(item.label) },
                selected = item.route == selectedItem.value,
                onClick = {
                    coroutineScope.launch { menuDrawerState.close() }
                    navController.navigate(item.route)
                    selectedItem.value = item.route
                },
                modifier = Modifier
                    .padding(NavigationDrawerItemDefaults.ItemPadding)
                    .width(280.dp)
                    .drawBehind {
                        if (item.route == selectedItem.value) {
                            drawLine(
                                color = selectedColor,
                                start = Offset(-24f, 0f),
                                end = Offset(-24f, size.height),
                                strokeWidth = borderWidth
                            )
                        }
                    }
                ,
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = Color.White,
                    unselectedContainerColor = Color.White,
                    selectedTextColor = selectedColor
                )
            )

        }
    }
}