package cz.ackee.testtask.characters.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.ackee.testtask.characters.data.CharacterRepository
import cz.ackee.testtask.characters.domain.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val characterRepository: CharacterRepository,
) : ViewModel() {
    private val _screenLoadingState = MutableStateFlow(true)
    private val _characters = MutableStateFlow<List<Character>>(emptyList())

    val screenLoadingState = _screenLoadingState.asStateFlow()
    val characters = _characters.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            characterRepository.getFavorites().collect { characters ->
                _characters.value = characters
                _screenLoadingState.value = false
            }
        }
    }
}
