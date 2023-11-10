package cz.ackee.testtask.characters.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import cz.ackee.testtask.characters.domain.Character
import kotlinx.coroutines.flow.Flow

private const val PAGE_SIZE = 40
class CharacterRepository(
    private val characterLocalDataSource: CharacterLocalDataSource,
    private val characterRemoteDataSource: CharacterRemoteDataSource,
    private val characterListPagingDataSource: CharacterListPagingDataSource,
) {
    // Loads characters from the remote data source.
    fun loadCharacters(): Flow<PagingData<Character>> {
        val config = PagingConfig(pageSize = PAGE_SIZE)
        return Pager(config) { characterListPagingDataSource }.flow
    }

    // Loads characters from remote data source matching by name
    fun searchCharacters(name: String): Flow<PagingData<Character>> {
        val config = PagingConfig(pageSize = PAGE_SIZE)
        return Pager(config) { CharacterSearchPagingDataSource(name = name, remoteDataSource = characterRemoteDataSource) }.flow
    }

    /* Gets a character from the local data source. If the character is not in the local data source, tries to get it from the remote data source.
     * If the character is not in the remote data source, returns null.
     */
    suspend fun getCharacter(id: Int): Character? {
        var character = characterLocalDataSource.getCharacter(id)
        return character
            ?: try {
                character = characterRemoteDataSource.getCharacter(id)
                return character
            } catch (_: Throwable) { null }
    }

    suspend fun getFavorites(): Flow<List<Character>> {
        return characterLocalDataSource.getFavorites()
    }

    suspend fun insert(character: Character) {
        characterLocalDataSource.insertCharacter(character)
    }

    suspend fun delete(character: Character) {
        characterLocalDataSource.deleteCharacter(character)
    }
}
