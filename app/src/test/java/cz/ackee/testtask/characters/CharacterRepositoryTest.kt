package cz.ackee.testtask.characters

import cz.ackee.testtask.characters.data.CharacterLocalDataSource
import cz.ackee.testtask.characters.data.CharacterRemoteDataSource
import cz.ackee.testtask.characters.data.CharacterRepository
import cz.ackee.testtask.characters.domain.Character
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

class CharacterRepositoryTest {

    private val characterLocalDataSource: CharacterLocalDataSource = mock()

    private val characterRemoteDataSource: CharacterRemoteDataSource = mock()

    private val characterRepository: CharacterRepository = CharacterRepository(characterLocalDataSource, characterRemoteDataSource)

    @Test
    fun `loadCharacters() should call the character remote data source and insert the characters into the local data source`() = runTest {
        val characters = listOf(mockCharacter())
        whenever(characterRemoteDataSource.getCharacters()).thenReturn(characters)

        characterRepository.loadCharacters()

        verify(characterRemoteDataSource).getCharacters()
        verify(characterLocalDataSource).insertCharacters(characters)
    }

    @Test
    fun `getAllCharacters() should return a flow of characters from the local data source`() = runTest {
        val characters = listOf(mockCharacter())
        whenever(characterLocalDataSource.getAllCharacters()).thenReturn(flowOf(characters))

        val collectedCharacters = characterRepository.getAllCharacters().toList().first()

        assertEquals(characters, collectedCharacters)
    }

    @Test
    fun `searchCharacters() should call the character remote data source and return a list of characters`() = runTest {
        val characters = listOf(mockCharacter())
        whenever(characterRemoteDataSource.searchCharacters(anyString())).thenReturn(characters)

        val searchedCharacters = characterRepository.searchCharacters("Rick Sanchez")

        assertEquals(characters, searchedCharacters)
    }

    @Test
    fun `getCharacter() should return a character from the local data source if it exists, otherwise from the remote data source`() = runTest {
        val character = mockCharacter()

        // Check if the character exists in the local data source.
        whenever(characterLocalDataSource.getCharacter(character.id)).thenReturn(character)

        val localCharacter = characterRepository.getCharacter(character.id)

        assertEquals(character, localCharacter)

        // Check if the character does not exist in the local data source.
        whenever(characterLocalDataSource.getCharacter(character.id)).thenReturn(null)
        whenever(characterRemoteDataSource.getCharacter(character.id)).thenReturn(character)

        val remoteCharacter = characterRepository.getCharacter(character.id)

        assertEquals(character, remoteCharacter)
    }

    @Test
    fun `getFavorites() should return a flow of favorite characters from the local data source`() = runTest {
        val characters = listOf(mockCharacter())
        whenever(characterLocalDataSource.getFavorites()).thenReturn(flowOf(characters))

        val collectedCharacters = characterRepository.getFavorites().toList().first()

        assertEquals(characters, collectedCharacters)
    }

    @Test
    fun `update() should update the character in the local data source`() = runTest {
        val character = mockCharacter()

        characterRepository.update(character)

        verify(characterLocalDataSource).updateCharacter(character)
    }

    private fun mockCharacter(id: Int = 1) =
        Character(
            id = id,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            type = "",
            gender = "Male",
            origin = "Earth (C-137)",
            location = "Citadel of Ricks",
            image = "",
        )
}
