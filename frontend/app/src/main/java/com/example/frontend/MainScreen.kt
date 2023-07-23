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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.frontend.ui.screens.auth.LoginScreen
import com.example.frontend.ui.screens.auth.RegistrationScreen
import com.example.frontend.ui.screens.forum.ForumScreen
import com.example.frontend.ui.screens.forum.ForumViewModel
import com.example.frontend.ui.screens.forum.PostCreation
import com.example.frontend.ui.screens.forum.ViewThread
import com.example.frontend.ui.screens.home.EventsScreen
import com.example.frontend.ui.screens.home.EventsViewModel
import com.example.frontend.ui.screens.secondscreen.CalendarScreen


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

    object ViewPost :
        ForumNavGraph("view_thread/{threadId}", Icons.Rounded.Person, R.string.viewThread)
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

                val eventsViewModel: EventsViewModel = viewModel()

                EventsScreen(navController, eventsViewModel) { eventsViewModel.getAllEvents() }
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


fun NavGraphBuilder.forumNavGraph(navController: NavHostController) {

    navigation(
        startDestination = ForumNavGraph.Posts.route, // this is the first screen you go to when you open auth_graph
        route = RootNavGraph.Forum.route // this is the URL to get to this navGraph
    ) {
        composable(route = ForumNavGraph.Posts.route) { entry ->
            val context = LocalContext.current
            val forumViewModel: ForumViewModel =
                entry.sharedViewModel<ForumViewModel>(navController = navController)
            ForumScreen(
                forumUiState = forumViewModel.forumUiState,
                retryAction = { forumViewModel.getAllThreads() },
                onCreateThread = { navController.navigate(ForumNavGraph.CreatePost.route) },
                navController = navController
            )
        }
        composable(route = ForumNavGraph.CreatePost.route) {
            val context = LocalContext.current

            PostCreation(
                onBack = { navController.navigate(ForumNavGraph.Posts.route) }
            )
        }

        composable(route = "viewThread") { navBackStackEntry ->
            val context = LocalContext.current

            val forumViewModel: ForumViewModel =
                navBackStackEntry.sharedViewModel<ForumViewModel>(navController = navController)

            ViewThread(
                forumViewModel = forumViewModel,
                onBack = { navController.navigate(ForumNavGraph.Posts.route) },
                forumUiState = forumViewModel.forumUiState
            )

        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController,
): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }

    return viewModel(parentEntry)
}

//fun NavGraphBuilder.createPostNavGraph(navController: NavController) {
//    navigation(
//        startDestination = ForumNavGraph.CreatePost.route,
//        route = ForumNavGraph.Posts.route
//    ) {
//        composable(route = ForumNavGraph.CreatePost.route) {
//            val context = LocalContext.current
//            ForumNavGraph.CreatePost
//        }
//    }
//}

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



