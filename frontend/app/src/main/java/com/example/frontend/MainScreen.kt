package com.example.frontend

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.frontend.navigation.RootNavGraph
import com.example.frontend.navigation.authNavGraph
import com.example.frontend.navigation.forumNavGraph
import com.example.frontend.ui.screens.CalendarScreen
import com.example.frontend.ui.screens.home.HomeScreen
import dagger.hilt.android.AndroidEntryPoint

@Composable
fun MainApp(
    // viewModel: MyViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: RootNavGraph.Home.route

    Scaffold(
        bottomBar = {
            AppBottomNavigationBar(
                currentRoute = currentRoute,
                onItemSelected = { route ->
                    navController.navigate(route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // re-selecting the same item
                        launchSingleTop = true
                        // Restore state when re-selecting a previously selected item
                        restoreState = true

                    }
                }
            )
        }
    ) { innerPadding ->
        // val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = RootNavGraph.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = RootNavGraph.Home.route) {
                val context = LocalContext.current
                HomeScreen(navController)
            }

            composable(route = RootNavGraph.Calendar.route) {
                val context = LocalContext.current
                CalendarScreen(navController = navController)
            }
            forumNavGraph(navController)

            authNavGraph(navController) // right now, this is the fourth button at the bottom
        }
    }
}


val bottomNavList = listOf(
    RootNavGraph.Home,
    RootNavGraph.Calendar,
    RootNavGraph.Forum,
    RootNavGraph.Auth
)


@Composable
fun AppBottomNavigationBar(
    currentRoute: String?,
    onItemSelected: (String) -> Unit
) {
    NavigationBar {
        bottomNavList.map { bottomNavItem ->
            NavigationBarItem(

                selected = currentRoute == bottomNavItem.route,
                onClick = { onItemSelected(bottomNavItem.route) },
                icon = {
                    Icon(
                        imageVector = bottomNavItem.icon, contentDescription = bottomNavItem.route
                    )
                },
                label = { bottomNavItem.route },
                modifier = Modifier.testTag(bottomNavItem.route)   //added testtag for UI testing
            )

        }
    }
}



