package cz.ackee.testtask.characters.presentation.detail

import cz.ackee.testtask.characters.MainCoroutineRule
import cz.ackee.testtask.characters.data.CharacterRepository
import cz.ackee.testtask.characters.domain.Character
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule(UnconfinedTestDispatcher())

    private lateinit var classUnderTest: DetailViewModel

    @Mock
    private lateinit var characterRepository: CharacterRepository

    @Before
    fun setUp() {
        classUnderTest = DetailViewModel(characterRepository)
        runBlocking {
            whenever(characterRepository.getCharacter(0)).thenReturn(mockCharacter())
        }
    }


    @Test
    fun `Given 0 when getCharacter then sets character to fetched character`() = runTest {
        // Given
        val id = 0

        // When
        classUnderTest.getCharacter(id)
        advanceUntilIdle()
        // Then
        assert(classUnderTest.character.value == mockCharacter())
    }
    @Test
    fun `Given non-existing id when getCharacter then sets character to null`() = runTest {
        // Given
        val id = 1

        // When
        classUnderTest.getCharacter(id)

        // Then
        assertNull(classUnderTest.character.value)
    }

    @Test
    fun `Given character is set when onFavoriteClick then change favorite`() = runTest  {
        // Given
        val id = 0
        classUnderTest.getCharacter(id)

        // When
        classUnderTest.onFavoriteClick()

        // Then
        assert(classUnderTest.character.value!!.favorite)
        verify(characterRepository).insert(classUnderTest.character.value!!)

        // When
        classUnderTest.onFavoriteClick()

        // Then
        assert(!classUnderTest.character.value!!.favorite)
        verify(characterRepository).delete(classUnderTest.character.value!!)
    }
}
fun mockCharacter(id: Int = 0) : Character = Character(id, "Rick Sanchez", "Alive","Human", "-", "Male", "Earth (c-137)", "Citadel of Ricks", "")

