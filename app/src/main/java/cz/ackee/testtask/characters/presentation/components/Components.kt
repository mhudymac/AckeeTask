package cz.ackee.testtask.characters.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import cz.ackee.testtask.R
import cz.ackee.testtask.characters.domain.Character
import java.io.IOException

@Composable
fun FullScreenCircularLoading() {
    Box(modifier = Modifier.fillMaxSize().padding(10.dp), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
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
                placeholder = painterResource(id = R.drawable.ic_placeholder),

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

@Composable
fun ListTopAppBar(
    onSearchClicked: () -> Unit,
) {
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
                            onSearchClicked.invoke()
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
}

@Composable
fun ListScaffold(
    loadingState: Boolean,
    favoriteState: Boolean,
    onCharacterButtonClicked: () -> Unit,
    onFavoriteButtonClicked: () -> Unit,
    onSearchButtonClicked: () -> Unit,
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
            ListTopAppBar(onSearchClicked = onSearchButtonClicked)
        },
        bottomBar = {
            BottomBar(
                onCharactersClicked = onCharacterButtonClicked,
                onFavoritesClicked = onFavoriteButtonClicked,
                favoriteState = favoriteState,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
        ) {
            if (loadingState) {
                FullScreenCircularLoading()
            } else {
                content()
            }
        }
    }
}

@Composable
fun ErrorScreen(
    text: String,
    showButton: Boolean = false,
    onRetryClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Text(text = text)
        if (showButton) {
            Button(onClick = onRetryClick) {
                Text(
                    text = "retry",
                )
            }
        }
    }
}

@Composable
fun PagingList(
    characters: LazyPagingItems<Character>,
    onCharacterClicked: (Int) -> Unit,
    padding: PaddingValues = PaddingValues(0.dp),
) {
    if (characters.loadState.refresh is LoadState.Error) {
        if ((characters.loadState.refresh as LoadState.Error).error is IOException) {
            ErrorScreen(
                text = "Unable to load characters",
                onRetryClick = { characters.retry() },
                showButton = true,
            )
        } else {
            ErrorScreen(text = "No characters matching the prompt")
        }
    } else if ((characters.loadState.refresh is LoadState.Loading) || (characters.loadState.mediator?.refresh is LoadState.Loading)) {
        FullScreenCircularLoading()
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = if (characters.itemCount == 0) Arrangement.Center else Arrangement.Top,
        ) {
            items(characters.itemCount) { index ->
                characters[index]?.let { character ->
                    CharacterListItem(
                        character = character,
                        onCharacterClicked = onCharacterClicked,
                    )
                }
            }
            item {
                if ((characters.loadState.append is LoadState.Loading) || (characters.loadState.mediator?.append is LoadState.Loading)) {
                    FullScreenCircularLoading()
                } else if (characters.loadState.append is LoadState.Error && (characters.loadState.append as LoadState.Error).error is IOException) {
                    ErrorScreen(
                        text = "Unable to load more characters",
                        onRetryClick = { characters.retry() },
                        showButton = true,
                    )
                }
            }
        }
    }
}
