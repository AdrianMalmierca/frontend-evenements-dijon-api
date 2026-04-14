package com.adrianmalmierca.dijonevents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.adrianmalmierca.dijonevents.ui.auth.AuthViewModel
import com.adrianmalmierca.dijonevents.ui.auth.LoginScreen
import com.adrianmalmierca.dijonevents.ui.auth.RegisterScreen
import com.adrianmalmierca.dijonevents.ui.events.EventDetailScreen
import com.adrianmalmierca.dijonevents.ui.events.EventsListScreen
import com.adrianmalmierca.dijonevents.ui.events.EventsViewModel
import com.adrianmalmierca.dijonevents.ui.events.MapScreen
import com.adrianmalmierca.dijonevents.ui.favorites.FavoritesScreen
import com.adrianmalmierca.dijonevents.ui.theme.DijonEventsTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class Screen(val route: String, val label: String) {
    object Login : Screen("login", "Connexion")
    object Register : Screen("register", "Inscription")
    object Events : Screen("events", "Événements")
    object Map : Screen("map", "Carte")
    object Favorites : Screen("favorites", "Favoris")
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DijonEventsTheme {
                DijonEventsNavHost()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DijonEventsNavHost() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val eventsViewModel: EventsViewModel = hiltViewModel()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    val bottomNavItems = listOf(
        Triple(Screen.Events, Icons.Default.List, "Événements"),
        Triple(Screen.Map, Icons.Default.Map, "Carte"),
        Triple(Screen.Favorites, Icons.Default.Favorite, "Favoris")
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState() //react state of the navigation
    val currentRoute = navBackStackEntry?.destination?.route //to get log in, events... To know in which screen we're
    val showBottomBar = currentRoute in listOf(Screen.Events.route, Screen.Map.route, Screen.Favorites.route)

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate(Screen.Events.route) {
                /*In rememberNavController it hasn't finished yet, and  currentBackStackEntryAsState doesn't have value yet
                 so navBackStackEntry = null*/
                popUpTo(navController.graph.findStartDestination().id) { inclusive = true } //we get the destination root and we include the destiny too
                //To navigate to Events and delete everything before
                //cause if we're in events and we go back, we can go to log in, and we don't want that, so to avoid that situation
            }
        } else {
            navController.navigate(Screen.Login.route) {
                popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            if (showBottomBar) {
                TopAppBar(
                    title = { Text("🍷 Dijon Événements") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    actions = {
                        TextButton(onClick = { authViewModel.logout() }) {
                            Text("Déconnexion", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { (screen, icon, label) ->
                        NavigationBarItem(
                            icon = { Icon(icon, contentDescription = label) },
                            label = { Text(label) },
                            //hierarchy, the navigation can have hierarchy, it includes all the parent + the actual one
                            //any { it.route == screen.route } to check if we're in the screen or in a child
                            //true because can be null
                            selected = navBackStackEntry?.destination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true } //to save the state of previous windows
                                    launchSingleTop = true // to avoid duplicates in the stack -> Events -> click Events -> click Events
                                    restoreState = true //restore the state keeps in saveState
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost( //to define which screens exist and which one is shown depending the actual route
            navController = navController,
            startDestination = if (isLoggedIn) Screen.Events.route else Screen.Login.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    viewModel = authViewModel,
                    onNavigateToRegister = { navController.navigate(Screen.Register.route) }
                )
            }
            composable(Screen.Register.route) {
                RegisterScreen(
                    viewModel = authViewModel,
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }
            composable(Screen.Events.route) {
                EventsListScreen(
                    viewModel = eventsViewModel,
                    onNavigateToDetail = { uid -> navController.navigate("detail/$uid") }
                )
            }
            composable(Screen.Map.route) {
                MapScreen(viewModel = eventsViewModel)
            }
            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    viewModel = eventsViewModel,
                    onNavigateToDetail = { uid -> navController.navigate("detail/$uid") }
                )
            }
            composable(
                route = "detail/{uid}",
                arguments = listOf(navArgument("uid") { type = NavType.StringType })
            ) { backStackEntry ->
                //takes the uid from the url, si es null, sales del composable
                val uid = backStackEntry.arguments?.getString("uid") ?: return@composable
                EventDetailScreen(
                    uid = uid,
                    viewModel = eventsViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
