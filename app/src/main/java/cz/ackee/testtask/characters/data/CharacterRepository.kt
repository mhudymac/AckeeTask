package cz.ackee.testtask.characters.data

import cz.ackee.testtask.characters.domain.Character
import kotlinx.coroutines.flow.Flow

class CharacterRepository(
    private val characterLocalDataSource: CharacterLocalDataSource,
    private val characterRemoteDataSource: CharacterRemoteDataSource,
) {
    /* Loads characters from the remote data source and inserts them into the local data source. If there is an error loading the characters from the remote data source, do nothing.
     * Returns a flow of all characters in the local data source.
     */
    suspend fun loadCharacters(): Flow<List<Character>> {
        try {
            val characters = characterRemoteDataSource.getCharacters()
            characterLocalDataSource.insertCharacters(characters)
        } catch (_: Throwable) { }

        return characterLocalDataSource.getAllCharacters()
    }

    suspend fun getAllCharacters(): Flow<List<Character>> {
        return characterLocalDataSource.getAllCharacters()
    }

    suspend fun searchCharacters(name: String): List<Character> {
        return try {
            characterRemoteDataSource.searchCharacters(name)
        } catch (e: Exception) {
            emptyList()
        }
    }

    /* Gets a character from the local data source. If the character is not in the local data source, tries to get it from the remote data source.
     * If the character is not in the remote data source, returns null.
     */
    suspend fun getCharacter(id: Int): Character? {
        var character = characterLocalDataSource.getCharacter(id)
        return if (character != null) {
            character
        } else {
            try {
                character = characterRemoteDataSource.getCharacter(id)
                characterLocalDataSource.insertCharacters(listOf(character))
                return character
            } catch (_: Throwable) { }
            null
        }
    }

    suspend fun getFavorites(): Flow<List<Character>> {
        return characterLocalDataSource.getFavorites()
    }

    suspend fun update(character: Character) {
        characterLocalDataSource.updateCharacter(character)
    }
}
