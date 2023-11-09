package cz.ackee.testtask.characters.presentation.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import cz.ackee.testtask.characters.domain.Character
import cz.ackee.testtask.characters.presentation.components.CharacterListItem
import cz.ackee.testtask.characters.presentation.components.ListScaffold
import cz.ackee.testtask.characters.presentation.components.ErrorScreen
import cz.ackee.testtask.navigation.Screen
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoriteScreen(
    navController: NavController,
    viewModel: FavoriteViewModel = koinViewModel(),
) {
    val characters by viewModel.characters.collectAsState()
    val loadingState by viewModel.screenLoadingState.collectAsState()

    ListScaffold(
        loadingState = loadingState,
        favoriteState = true,
        onCharacterButtonClicked = { navController.navigate(Screen.ListScreen.route) },
        onFavoriteButtonClicked = {},
        onSearchButtonClicked = { navController.navigate(Screen.SearchScreen.route) },
    ) {
        FavoriteScreen(
            characters = characters,
            onCharacterClicked = { navController.navigate(Screen.DetailScreen.route + "/$it") },
        )
    }
}

@Composable
private fun FavoriteScreen(
    characters: List<Character>,
    onCharacterClicked: (Int) -> Unit,
) {
    if (characters.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            items(characters) { character ->
                CharacterListItem(
                    character = character,
                    onCharacterClicked = onCharacterClicked,
                )
            }
        }
    } else {
        ErrorScreen(text = "No favorite characters", showButton = false)
    }
}
