package com.example.frontend

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.frontend.ui.ForumScreen
import com.example.frontend.ui.HomeScreen

enum class AppScreen(@StringRes val title: Int) {
    Home(title = R.string.home),
    Forum(title = R.string.forum),
    Social(title = R.string.social),
    Links(title = R.string.links),
    Acads(title = R.string.acads)
}

@Composable
fun MainApp(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = AppScreen.valueOf(
        backStackEntry?.destination?.route ?: AppScreen.Home.name
    )

    NavHost(
        navController = navController,
        startDestination = AppScreen.Home.name,
        modifier = Modifier
    ) {
        composable(route = AppScreen.Home.name) {
            HomeScreen()
        }
        composable(route = AppScreen.Forum.name) {
            ForumScreen()
        }
    }

}


