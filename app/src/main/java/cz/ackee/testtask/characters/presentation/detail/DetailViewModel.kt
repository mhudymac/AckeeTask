package cz.ackee.testtask.characters.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.ackee.testtask.characters.data.CharacterRepository
import cz.ackee.testtask.characters.domain.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val characterRepository: CharacterRepository
) : ViewModel() {
    private val _character = MutableStateFlow<Character?>(null)
    val character: StateFlow<Character?> = _character

    private val _loading = MutableStateFlow(true)
    val loading = _loading.asStateFlow()

    fun getCharacter(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _character.value = characterRepository.getCharacter(id)
            _loading.value = false
        }
    }

    // Toggles the favorite status of the current character and updates it in the repository.
    fun onFavoriteClick() {
        val favorite = character.value?.favorite?.not() ?: false
        _character.value = character.value?.copy(favorite = favorite)

        viewModelScope.launch(Dispatchers.IO) {
            val tmpCharacter = _character.value
            if (tmpCharacter != null) {
                characterRepository.update(tmpCharacter)
            }
        }
    }
}
