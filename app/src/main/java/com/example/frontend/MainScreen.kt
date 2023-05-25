package com.example.frontend

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.frontend.ui.screens.ForumScreen
import com.example.frontend.ui.screens.HomeScreen
import com.example.frontend.ui.screens.LoginScreen


data class BottomNavItem(
    val name: String,
    val icon: ImageVector,
)

// TODO implement the remaining navi screens to uncomment these BottomNavItems
val bottomNavItems = listOf(
    BottomNavItem(
        name = AppScreen.Home.name,
        icon = Icons.Rounded.Home
    ),
    BottomNavItem(
        name = AppScreen.Forum.name,
        icon = Icons.Rounded.Person
    ),
    BottomNavItem(
        name = AppScreen.Login.name,
        icon = Icons.Rounded.Lock
    ),
//    BottomNavItem(
//        name = AppScreen.Social.name,
//        icon = Icons.Rounded.ShoppingCart
//    ),
//    BottomNavItem(
//        name = AppScreen.Links.name,
//        icon = Icons.Rounded.ShoppingCart
//    ),
//    BottomNavItem(
//        name = AppScreen.Acads.name,
//        icon = Icons.Rounded.ShoppingCart
//    ),
)


enum class AppScreen(@StringRes val title: Int) {
    Home(title = R.string.home),
    Forum(title = R.string.forum),
    Social(title = R.string.social),
    Links(title = R.string.links),
    Acads(title = R.string.acads),
    Login(title = R.string.login)
}

@Composable
fun MainApp(
    // viewModel: MyViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: AppScreen.Home.name



    Scaffold(
        bottomBar = {
            AppBottomNavigationBar(
                currentRoute = currentRoute,
                onItemSelected = { route ->
                    navController.navigate(route) {
                    }
                }
            )
        }
    ) { innerPadding ->
        // TODO: what even is viewmodel
        // TODO: when to use context and not
        // val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = AppScreen.Home.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = AppScreen.Home.name) {
                val context = LocalContext.current
                HomeScreen()
            }
            composable(route = AppScreen.Forum.name) {
                val context = LocalContext.current
                ForumScreen()
            }
            composable(route = AppScreen.Login.name) {
                val context = LocalContext.current
                LoginScreen { navController.navigate(AppScreen.Login.name) }
            }
        }
    }
}


@Composable
fun AppBottomNavigationBar(
    currentRoute: String?,
    onItemSelected: (String) -> Unit
) {
    NavigationBar {
        bottomNavItems.map { bottomNavItem ->
            NavigationBarItem(
                selected = currentRoute == bottomNavItem.name,
                onClick = { onItemSelected(bottomNavItem.name) },
                icon = { Icon(imageVector = bottomNavItem.icon, contentDescription = null) },
                label = { bottomNavItem.name }
            )
        }
    }
}



