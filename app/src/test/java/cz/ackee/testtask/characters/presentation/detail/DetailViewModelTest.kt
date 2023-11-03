package cz.ackee.testtask.characters.presentation.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import cz.ackee.testtask.characters.data.CharacterRepository
import cz.ackee.testtask.characters.domain.Character
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
class DetailViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val characterRepository: CharacterRepository = mock()
    private val viewModel: DetailViewModel = DetailViewModel(characterRepository)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getCharacter() should correctly fetch the character from the repository and set the character and loading properties accordingly`() = runTest {
        val unconfinedTestDispatcher = UnconfinedTestDispatcher(testScheduler)

        val fakeCharacter = mockCharacter()
        whenever(characterRepository.getCharacter(1)).thenReturn(fakeCharacter)

        runTest(unconfinedTestDispatcher) {
            viewModel.getCharacter(1)
        }

        advanceUntilIdle()

        assertEquals(fakeCharacter, viewModel.character.value)
        assertFalse(viewModel.loading.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `onFavoriteClick() should correctly update the character's favorite status`() = runTest {
        val unconfinedTestDispatcher = UnconfinedTestDispatcher(testScheduler)

        val fakeCharacter = mockCharacter()
        whenever(characterRepository.getCharacter(1)).thenReturn(fakeCharacter)

        runTest(unconfinedTestDispatcher) {
            viewModel.getCharacter(1)
        }
        advanceUntilIdle()

        viewModel.onFavoriteClick()

        assertEquals(true, viewModel.character.value!!.favorite)
    }
}
private fun mockCharacter(characterId: Int = 1) =
    Character(
        id = characterId,
        name = "Rick Sanchez",
        status = "Alive",
        species = "Human",
        type = "",
        gender = "Male",
        origin = "Earth (C-137)",
        location = "Citadel of Ricks",
        image = "",
    )
