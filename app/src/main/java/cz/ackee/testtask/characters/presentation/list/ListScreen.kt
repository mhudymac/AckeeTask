package cz.ackee.testtask.characters.presentation.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import cz.ackee.testtask.characters.domain.Character
import cz.ackee.testtask.characters.presentation.components.ListScaffold
import cz.ackee.testtask.characters.presentation.components.PagingList
import cz.ackee.testtask.navigation.Screen
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListScreen(
    navController: NavController,
    viewModel: ListViewModel = koinViewModel(),
) {
    val characters = viewModel.characters.collectAsLazyPagingItems()

    ListScaffold(
        loadingState = false,
        favoriteState = false,
        onCharacterButtonClicked = {},
        onFavoriteButtonClicked = { navController.navigate(Screen.FavoriteScreen.route) },
        onSearchButtonClicked = { navController.navigate(Screen.SearchScreen.route) },
    ) {
        ListScreen(
            characters = characters,
            onCharacterClicked = { navController.navigate(Screen.DetailScreen.route + "/$it") },
        )
    }
}

@Composable
private fun ListScreen(
    characters: LazyPagingItems<Character>,
    onCharacterClicked: (Int) -> Unit,
) {
    PagingList(characters = characters, onCharacterClicked = onCharacterClicked)
}
