package cz.ackee.testtask.characters.data

import cz.ackee.testtask.characters.domain.Character
import kotlinx.coroutines.flow.Flow

interface CharacterLocalDataSource {
    suspend fun getAllCharacters(): Flow<List<Character>>

    suspend fun getCharacter(id: Int): Character?

    suspend fun getFavorites(): Flow<List<Character>>

    suspend fun insertCharacters(characters: List<Character>)

    suspend fun deleteCharacters()

    suspend fun updateCharacter(character: Character)
}
