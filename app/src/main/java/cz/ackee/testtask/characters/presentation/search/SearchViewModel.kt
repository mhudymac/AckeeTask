package cz.ackee.testtask.characters.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.ackee.testtask.characters.data.CharacterRepository
import cz.ackee.testtask.characters.domain.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val _characters = MutableStateFlow(emptyList<Character>())
    val characters: StateFlow<List<Character>> = _characters

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    val searchText: MutableStateFlow<String> = MutableStateFlow("")

    fun searchCharacters(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            _characters.value = if (name.isNotEmpty()) characterRepository.searchCharacters(name) else emptyList()
            _loading.value = false
        }
        searchText.value = name
    }

    fun clearText() {
        searchText.value = ""
    }
}
