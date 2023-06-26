package com.example.brewbuddy
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.brewbuddy.ui.theme.GreyLight
import com.example.brewbuddy.ui.theme.SlateLight
import androidx.compose.runtime.*
import com.example.brewbuddy.ui.theme.GreyMedium

sealed class BottomNavigationScreens(val route: String, @StringRes val resourceId: Int, @DrawableRes val icon: Int) {
    object Profile : BottomNavigationScreens("Profile", R.string.profile_route, R.drawable.icon_user)
    object ShopLocator : BottomNavigationScreens("ShopLocator", R.string.shop_locator_route, R.drawable.icon_locator)
    object Marketplace : BottomNavigationScreens("Marketplace", R.string.marketplace_route, R.drawable.icon_store)
    object Featured : BottomNavigationScreens("Featured", R.string.featured_route, R.drawable.icon_explore)
    object Recipes : BottomNavigationScreens("Recipes", R.string.recipes_route, R.drawable.icon_page)
}

@Composable
fun BottomNavigation(
    navController: NavHostController,
    items: List<BottomNavigationScreens>
) {

    CenteredBottomAppBar(
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        val currentRoute = currentRoute(navController)
        var active by remember { mutableStateOf(BottomNavigationScreens.Profile.route) }
        items.forEach { item ->
            IconButton(
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route)
                        active = item.route
                    }
                },
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
            ) {
                Icon(
                    painter = painterResource(id = item.icon),
                    contentDescription = "",
                    tint = if (active == item.route) GreyMedium else SlateLight,
                )

            }

        }
    }
}

@Composable
private fun CenteredBottomAppBar(
    containerColor: Color = BottomAppBarDefaults.containerColor,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        color = containerColor,
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(80.0.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}
@Composable
private fun currentRoute(navController: NavHostController): String? {
    return navController.currentBackStackEntry?.destination?.route
}