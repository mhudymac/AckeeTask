package cz.ackee.testtask.characters.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import cz.ackee.testtask.R
import cz.ackee.testtask.characters.domain.Character
import cz.ackee.testtask.navigation.Screen
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListScreen(
    navController: NavController,
    viewModel: ListViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    ListScreen(
        loadingState = state.screenLoading,
        favoriteState = state.screenFavorite,
        navController = navController,
        characters = state.characters,
        onCharactersClicked = { viewModel.getCharacters() },
        onFavoriteClicked = { viewModel.getFavorites() },

    )
}

@Composable
private fun ListScreen(
    loadingState: Boolean,
    favoriteState: Boolean,
    navController: NavController,
    characters: List<Character>,
    onCharactersClicked: () -> Unit,
    onFavoriteClicked: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(id = R.string.characters),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 25.sp,
                        )
                        Icon(
                            modifier = Modifier
                                .clickable {
                                    navController.navigate(Screen.SearchScreen.route)
                                },
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = stringResource(id = R.string.search),
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onBackground,
                elevation = 10.dp,
            )
        },
        bottomBar = {
            BottomBar(
                onCharactersClicked,
                onFavoriteClicked,
                favoriteState,
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
        ) {
            if (loadingState) {
                LoadingState()
            } else {
                LoadedState(
                    characters = characters,
                    onCharacterClicked = {
                        navController.navigate(Screen.DetailScreen.route + "/$it")
                    },
                )
            }
        }
    }
}

@Composable
fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun LoadedState(
    characters: List<Character>,
    onCharacterClicked: (Int) -> Unit,
) {
    if (characters.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            items(characters) { character ->
                CharacterListItem(
                    character = character,
                    onCharacterClicked = onCharacterClicked,
                )
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "No characters")
        }
    }
}

@Composable
fun BottomBar(
    onCharactersClicked: () -> Unit,
    onFavoritesClicked: () -> Unit,
    favoriteState: Boolean,
) {
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.primary,
    ) {
        val characterColor = if (!favoriteState) MaterialTheme.colors.secondary else MaterialTheme.colors.secondaryVariant
        val favoritesColor = if (favoriteState) MaterialTheme.colors.secondary else MaterialTheme.colors.secondaryVariant

        BottomNavigationItem(
            label = {
                Text(
                    text = stringResource(id = R.string.characters),
                    color = characterColor,
                )
            },
            onClick = onCharactersClicked,
            selected = !favoriteState,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_characters),
                    tint = characterColor,
                    contentDescription = stringResource(id = R.string.characters_navigation_icon),
                )
            },
        )
        BottomNavigationItem(
            label = {
                Text(
                    text = stringResource(id = R.string.favorites),
                    color = favoritesColor,
                )
            },
            selected = favoriteState,
            onClick = onFavoritesClicked,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_favorites_filled),
                    tint = favoritesColor,
                    contentDescription = stringResource(id = R.string.favorite_navigation_icon),
                )
            },
        )
    }
}

@Composable
fun CharacterListItem(
    character: Character,
    onCharacterClicked: (Int) -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            .clickable {
                onCharacterClicked(character.id)
            },
        elevation = 12.dp,
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
        ) {
            AsyncImage(
                model = character.image,
                contentDescription = stringResource(id = R.string.characters_avatar),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .size(64.dp),
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = character.name,
                        style = MaterialTheme.typography.h6,
                    )
                    if (character.favorite) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_favorites_filled),
                            tint = MaterialTheme.colors.secondary,
                            contentDescription = stringResource(id = R.string.favorite_icon),
                        )
                    }
                }
                Text(text = character.status)
            }
        }
    }
}
