package cz.ackee.testtask.characters.data

import cz.ackee.testtask.characters.presentation.detail.mockCharacter
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class CharacterRepositoryTest {
    private lateinit var classUnderTest: CharacterRepository

    @Mock
    private lateinit var characterLocalDataSource: CharacterLocalDataSource

    @Mock
    private lateinit var characterRemoteDataSource: CharacterRemoteDataSource

    @Mock
    private lateinit var characterListPagingDataSource: CharacterListPagingDataSource

    @Before
    fun setUp() {
        classUnderTest = CharacterRepository(characterLocalDataSource, characterRemoteDataSource, characterListPagingDataSource)
        runBlocking {
            whenever(characterLocalDataSource.getCharacter(0)).thenReturn(mockCharacter())
            whenever(characterRemoteDataSource.getCharacter(1)).thenReturn(mockCharacter(1))
        }
    }


    @Test
    fun `Given character is in local data source when getCharacter then should return the value from local data source`() = runTest {
        // Given
        val id = 0

        // When
        val actualValue = classUnderTest.getCharacter(id)

        // Then
        assertEquals(actualValue, mockCharacter())
        assertNotEquals(actualValue, mockCharacter(1))
    }

    @Test
    fun `Given character is not in local data source when getCharacter then should return the value from remote data source`() = runTest {
        // Given
        val id = 1

        // When
        val actualValue = classUnderTest.getCharacter(id)

        // Then
        assertNotEquals(actualValue, mockCharacter())
        assertEquals(actualValue, mockCharacter(1))
    }

    @Test
    fun `Given character is not in local or remote data source when getCharacter then should return null`() = runTest {
        // Given
        val id = 3

        // When
        val actualValue = classUnderTest.getCharacter(id)

        // Then
        assertNull(actualValue)
    }


    @Test
    fun `When insert then should call insert on local data source`() = runTest {
        // When
        classUnderTest.insert(mockCharacter())

        // Then
        verify(characterLocalDataSource).insertCharacter(mockCharacter())
    }


    @Test
    fun `When delete then should call delete on local data source`() = runTest {
        // When
        classUnderTest.delete(mockCharacter())

        // Then
        verify(characterLocalDataSource).deleteCharacter(mockCharacter())
    }

}
