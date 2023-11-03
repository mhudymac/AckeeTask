package cz.ackee.testtask.characters.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cz.ackee.testtask.R
import cz.ackee.testtask.characters.domain.Character
import cz.ackee.testtask.characters.presentation.list.CharacterListItem
import cz.ackee.testtask.characters.presentation.list.LoadingState
import cz.ackee.testtask.navigation.Screen
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: SearchViewModel = koinViewModel(),
) {
    val characters by viewModel.characters.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val searchedText by viewModel.searchText.collectAsState()

    SearchScreenContent(
        onNavigateBack = navController::popBackStack,
        searchedText = searchedText,
        characters = characters,
        loading = loading,
        onSearch = viewModel::searchCharacters,
        onClear = viewModel::clearText,
        onCharacterClicked = {
            navController.navigate(Screen.DetailScreen.route + "/$it")
        },
    )
}

@Composable
fun SearchScreenContent(
    searchedText: String,
    characters: List<Character>,
    onNavigateBack: () -> Unit,
    onSearch: (String) -> Unit,
    onClear: () -> Unit,
    onCharacterClicked: (Int) -> Unit,
    loading: Boolean,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                elevation = 20.dp,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .height(56.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.KeyboardArrowLeft,
                            contentDescription = stringResource(id = R.string.arrow_back),
                        )
                    }

                    SearchTopBarTitle(
                        searchedText,
                        onSearch,
                        onClear,
                    )
                }
            }
        },
    ) {
        if (loading || (characters.isEmpty() && searchedText.isNotEmpty())) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (loading) {
                    LoadingState()
                } else {
                    Text("No characters found matching your description :(")
                }
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(it)) {
                items(characters) { character ->
                    CharacterListItem(
                        character = character,
                        onCharacterClicked = onCharacterClicked,
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchTopBarTitle(
    text: String,
    onSearchTextChanged: (String) -> Unit,
    onClear: () -> Unit,
) {
    var showClearButton by remember { mutableStateOf(text.isNotEmpty()) }
    TextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = text,
        onValueChange = {
            onSearchTextChanged(it)
            showClearButton = it.isNotEmpty()
        },
        placeholder = {
            Text(text = stringResource(R.string.search))
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            backgroundColor = Color.Transparent,
            cursorColor = LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
        ),
        trailingIcon = {
            if (showClearButton) {
                IconButton(onClick = {
                    onClear()
                    showClearButton = false
                }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.clear_icon),
                    )
                }
            }
        },
    )
}
