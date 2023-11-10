package cz.ackee.testtask.characters.presentation.list

import androidx.lifecycle.ViewModel
import cz.ackee.testtask.characters.data.CharacterRepository

class ListViewModel(
    characterRepository: CharacterRepository,
) : ViewModel() {
    val characters = characterRepository.loadCharacters()
}
