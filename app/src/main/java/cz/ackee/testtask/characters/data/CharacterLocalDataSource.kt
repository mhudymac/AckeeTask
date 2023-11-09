package cz.ackee.testtask.characters.data

import cz.ackee.testtask.characters.domain.Character
import kotlinx.coroutines.flow.Flow

interface CharacterLocalDataSource {

    suspend fun getCharacter(id: Int): Character?

    suspend fun isFavorite(id: Int): Boolean

    suspend fun getFavorites(): Flow<List<Character>>

    suspend fun insertCharacter(character: Character)

    suspend fun deleteCharacters()

    suspend fun deleteCharacter(character: Character)
}
