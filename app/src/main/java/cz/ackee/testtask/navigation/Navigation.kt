package cz.ackee.testtask.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.ackee.testtask.characters.presentation.detail.DetailScreen
import cz.ackee.testtask.characters.presentation.list.FavoriteScreen
import cz.ackee.testtask.characters.presentation.list.ListScreen
import cz.ackee.testtask.characters.presentation.search.SearchScreen

// Defines the navigation graph for the app.
@Composable
fun Navigation() {
    val navController = rememberNavController()

    // Creates a navigation host that navigates between screens based on the current destination.
    NavHost(
        navController = navController,
        startDestination = Screen.ListScreen.route,
    ) {
        composable(route = Screen.ListScreen.route) {
            ListScreen(navController = navController)
        }
        composable(route = Screen.FavoriteScreen.route) {
            FavoriteScreen(navController = navController)
        }
        composable(
            route = Screen.DetailScreen.route + "/{id}",
            arguments = listOf(
                navArgument(name = "id") {
                    type = NavType.IntType
                },
            ),
        ) { entry ->
            DetailScreen(
                navController = navController,
                id = entry.arguments?.getInt("id"),
            )
        }
        composable(route = Screen.SearchScreen.route) {
            SearchScreen(navController = navController)
        }
    }
}
