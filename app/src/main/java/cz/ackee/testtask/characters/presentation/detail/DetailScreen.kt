package cz.ackee.testtask.characters.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import cz.ackee.testtask.R
import cz.ackee.testtask.characters.domain.Character
import cz.ackee.testtask.characters.presentation.list.LoadingState
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailScreen(
    navController: NavController,
    viewModel: DetailViewModel = koinViewModel(),
    id: Int?,
) {
    val character by viewModel.character.collectAsState()
    val loading by viewModel.loading.collectAsState()

    if (id != null) {
        viewModel.getCharacter(id)
    }

    DetailScreenContent(
        character = character,
        onFavorite = viewModel::onFavoriteClick,
        onNavigateBack = navController::popBackStack,
        loading = loading,
    )
}

@Composable
fun DetailScreenContent(
    character: Character?,
    onFavorite: () -> Unit,
    onNavigateBack: () -> Unit,
    loading: Boolean,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.arrow_back),
                        )
                    }
                },

                title = {
                    if (character != null) {
                        Text(text = character.name)
                    }
                },
                actions = {
                    if (character != null) {
                        IconButton(onClick = onFavorite) {
                            Icon(
                                painter = if (character.favorite) {
                                    painterResource(id = R.drawable.ic_favorites_filled)
                                } else {
                                    painterResource(
                                        id = R.drawable.ic_favorites,
                                    )
                                },
                                tint = MaterialTheme.colors.secondary,
                                contentDescription = stringResource(
                                    id = R.string.favorite_navigation_icon,
                                ),
                            )
                        }
                    }
                },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = if (character == null || loading) Arrangement.Center else Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (loading) {
                LoadingState()
            } else if (character == null) {
                Text(stringResource(R.string.no_character_loaded))
            } else {
                CharacterDetail(character)
            }
        }
    }
}

@Composable
fun CharacterDetail(character: Character) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
    ) {
        Row(modifier = Modifier.padding(all = 16.dp)) {
            AsyncImage(
                model = character.image,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .size(150.dp),
                contentDescription = stringResource(id = R.string.image),
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 8.dp,
                        bottom = 8.dp,
                    ),
            ) {
                Text(
                    text = stringResource(R.string.name),
                    style = MaterialTheme.typography.body1,
                )
                Text(
                    modifier = Modifier.padding(top = 16.dp),
                    text = character.name,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        Divider(
            color = MaterialTheme.colors.primaryVariant,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .width(1.dp),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
        ) {
            Information(title = stringResource(R.string.status), value = character.status)
            Information(title = stringResource(R.string.species), value = character.species)
            Information(title = stringResource(R.string.type), value = character.type)
            Information(title = stringResource(R.string.gender), value = character.gender)
            Information(title = stringResource(R.string.origin), value = character.origin)
            Information(title = stringResource(R.string.location), value = character.location)
        }
    }
}

@Composable
fun Information(title: String, value: String) {
    Column(
        modifier = Modifier.padding(top = 16.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.body1,
        )
        Text(
            modifier = Modifier.fillMaxWidth(0.5F),
            text = value.ifEmpty { "-" },
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.ExtraBold,
        )
    }
}
