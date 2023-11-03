package cz.ackee.testtask.characters.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.ackee.testtask.characters.data.CharacterRepository
import cz.ackee.testtask.characters.domain.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListViewModel(
    private val characterRepository: CharacterRepository,
) : ViewModel() {

    private val _screenLoadingState = MutableStateFlow(true)
    private val _screenFavoriteState = MutableStateFlow(false)

    // A flow of characters in the current screen state.
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _characters = _screenFavoriteState
        .flatMapLatest { favorite ->
            withContext(Dispatchers.IO) {
                val characters: Flow<List<Character>>
                // Get the characters from the database, depending on the current favorite state.
                if (favorite) {
                    characters = characterRepository.getFavorites()
                    _screenLoadingState.value = false
                } else {
                    characters = characterRepository.getAllCharacters()
                    _screenLoadingState.value = false
                }
                characters
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(ListState())

    val state = combine(_state, _screenLoadingState, _screenFavoriteState, _characters) { state, screenLoading, screenFavorite, characters ->
        state.copy(
            screenLoading = screenLoading,
            screenFavorite = screenFavorite,
            characters = characters,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ListState())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            characterRepository.loadCharacters()
        }
    }

    fun getFavorites() {
        if (_screenFavoriteState.value.not()) {
            _screenFavoriteState.value = true
            _screenLoadingState.value = true
        }
    }

    fun getCharacters() {
        if (!_screenFavoriteState.value.not()) {
            _screenFavoriteState.value = false
            _screenLoadingState.value = true
        }
    }
}

data class ListState(
    val screenLoading: Boolean = true,
    val screenFavorite: Boolean = false,
    val characters: List<Character> = emptyList(),
)
