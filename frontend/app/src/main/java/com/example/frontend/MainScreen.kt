package com.example.frontend

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.frontend.ui.screens.CalendarScreen
import com.example.frontend.ui.screens.ForumScreen
import com.example.frontend.ui.screens.ForumViewModel
import com.example.frontend.ui.screens.HomeScreen
import com.example.frontend.ui.screens.LoginScreen
import com.example.frontend.ui.screens.RegistrationScreen


sealed class RootNavGraph(val route: String, val icon: ImageVector, @StringRes val title: Int) {
    object Home : RootNavGraph("home", Icons.Rounded.Home, R.string.home)
    object Calendar : RootNavGraph("calendar", Icons.Rounded.Today, R.string.calendar)
    object Forum : RootNavGraph("forum", Icons.Rounded.Person, R.string.forum)
    object Auth : RootNavGraph("auth", Icons.Rounded.Lock, R.string.login)
}

val bottomNavList = listOf(
    RootNavGraph.Home,
    RootNavGraph.Calendar,
    RootNavGraph.Forum,
    RootNavGraph.Auth
)

sealed class AuthNavGraph(val route: String, val icon: ImageVector, @StringRes val title: Int) {
    object Login : AuthNavGraph("login", Icons.Rounded.Lock, R.string.login)
    object Registration : AuthNavGraph("registration", Icons.Rounded.Lock, R.string.registration)
}

sealed class ForumNavGraph(val route: String, val icon: ImageVector, @StringRes val title: Int) {
    object Posts : ForumNavGraph("posts", Icons.Rounded.Person, R.string.forum)
    object CreatePost : ForumNavGraph("create_post", Icons.Rounded.Person, R.string.addNewPost)
}


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

fun NavGraphBuilder.forumNavGraph(navController: NavController) {
    navigation(
        startDestination = ForumNavGraph.Posts.route, // this is the first screen you go to when you open auth_graph
        route = RootNavGraph.Forum.route // this is the URL to get to this navGraph
    ) {
        composable(route = ForumNavGraph.Posts.route) {
            val context = LocalContext.current
            val postViewModel: ForumViewModel = viewModel()
            ForumScreen(
                forumUiState = postViewModel.forumUiState,
                retryAction = { postViewModel.getAllPosts() },
            )
        }
//        composable(route = ForumNavGraph.CreatePost.route) {
//            val context = LocalContext.current
//            val postViewModel: PostsViewModel = viewModel()
//            ForumScreen(
//                postsUiState = postViewModel.postsUiState,
//                retryAction = { postViewModel.getAllPosts() },
//            )
//        }
    }
}


// TODO shared viewmodel
fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation(
        startDestination = AuthNavGraph.Login.route, // this is the first screen you go to when you open auth_graph
        route = RootNavGraph.Auth.route // this is the URL to get to this navGraph
    ) {
        composable(AuthNavGraph.Login.route) {
            val context = LocalContext.current
            LoginScreen(onNavigateToRegister = { navController.navigate(AuthNavGraph.Registration.route) })
        }
        composable(route = AuthNavGraph.Registration.route) {
            val context = LocalContext.current
            RegistrationScreen(onNavigateToLogin = { navController.navigate(AuthNavGraph.Login.route) })
        }
    }
}


@Composable
fun AppBottomNavigationBar(
    currentRoute: String?,
    onItemSelected: (String) -> Unit
) {
    NavigationBar {
        bottomNavList.map { bottomNavItem ->
            NavigationBarItem(

                selected = currentRoute == bottomNavItem.name,
                onClick = { onItemSelected(bottomNavItem.name) },
                icon = { Icon(imageVector = bottomNavItem.icon, contentDescription = bottomNavItem.name
                ) },
                label = { bottomNavItem.name },
                modifier = Modifier.testTag(bottomNavItem.name)   //added testtag for UI testing
            )

        }
    }
}



