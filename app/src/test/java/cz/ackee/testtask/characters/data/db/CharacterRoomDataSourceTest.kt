import cz.ackee.testtask.characters.data.db.CharacterDao
import cz.ackee.testtask.characters.data.db.CharacterRoomDataSource
import cz.ackee.testtask.characters.data.db.DbCharacter
import cz.ackee.testtask.characters.domain.Character
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.lang.reflect.Method

class CharacterRoomDataSourceTest {

    private val characterDao: CharacterDao = mock()
    private val characterLocalDataSource = CharacterRoomDataSource(characterDao)
    private val toDbMethod: Method =
        characterLocalDataSource.javaClass.getDeclaredMethod("toDb", Character::class.java)
            .apply { isAccessible = true }

    @Test
    fun `getAllCharacters() should return a flow of characters from the database`() = runTest {
        val characters = listOf(mockCharacter(1))
        val dbCharacters = listOf(mockDbCharacter(1))
        whenever(characterDao.getAllCharacters()).thenReturn(flowOf(dbCharacters))

        val collectedCharacters = characterLocalDataSource.getAllCharacters().toList().first()

        assertEquals(characters, collectedCharacters)
    }

    @Test
    fun `getCharacter() should return a character from the database if it exists`() = runTest {
        val character = mockCharacter(1)
        val dbCharacter = mockDbCharacter(1)
        whenever(characterDao.getCharacter(character.id)).thenReturn(dbCharacter)

        val fetchedCharacter = characterLocalDataSource.getCharacter(character.id)

        assertEquals(character, fetchedCharacter)
    }

    @Test
    fun `getCharacter() should return null if the character does not exist in the database`() = runTest {
        whenever(characterDao.getCharacter(1)).thenReturn(null)

        val fetchedCharacter = characterLocalDataSource.getCharacter(1)

        assertNull(fetchedCharacter)
    }

    @Test
    fun `insertCharacters() should insert the characters into the database`() = runTest {
        val characters = listOf(mockCharacter(1), mockCharacter(2))
        val dbCharacters = listOf(mockDbCharacter(1), mockDbCharacter(2))

        characterLocalDataSource.insertCharacters(characters)

        verify(characterDao).insertCharacters(dbCharacters)
    }

    @Test
    fun `deleteCharacters() should delete all characters from the database`() = runTest {
        characterLocalDataSource.deleteCharacters()

        verify(characterDao).deleteCharacters()
    }

    @Test
    fun `updateCharacter() should update the character in the database`() = runTest {
        val character = mockCharacter(1)
        val dbCharacter = mockDbCharacter(1)

        characterLocalDataSource.updateCharacter(character)

        verify(characterDao).updateCharacter(dbCharacter)
    }

    private fun mockCharacter(id: Int = 1) =
        Character(id, "Rick Sanchez", "Alive", "Human", "", "Male", "Earth (C-137)", "Citadel of Ricks", "", false)
    private fun mockDbCharacter(id: Int = 1): DbCharacter {
        return toDbMethod.invoke(characterLocalDataSource, mockCharacter(id)) as DbCharacter
    }
}
