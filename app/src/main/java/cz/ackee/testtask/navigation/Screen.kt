package cz.ackee.testtask.navigation

sealed class Screen(val route: String) {
    object ListScreen : Screen("list")
    object DetailScreen : Screen("detail")
    object SearchScreen : Screen("search")
}
