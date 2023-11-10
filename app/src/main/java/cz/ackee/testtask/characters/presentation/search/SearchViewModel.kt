package cz.ackee.testtask.characters.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cz.ackee.testtask.characters.data.CharacterRepository
import cz.ackee.testtask.characters.domain.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val characterRepository: CharacterRepository,
) : ViewModel() {

    private val _characters: MutableStateFlow<PagingData<Character>> = MutableStateFlow(PagingData.empty())
    val characters = _characters

    val searchText: MutableStateFlow<String> = MutableStateFlow("")

    fun searchCharacters(name: String) {
        if (name.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                characterRepository.searchCharacters(name).cachedIn(viewModelScope).collect { characters ->
                    _characters.value = characters
                }
            }
        }
        searchText.value = name
    }

    fun clearText() {
        searchText.value = ""
    }
}
