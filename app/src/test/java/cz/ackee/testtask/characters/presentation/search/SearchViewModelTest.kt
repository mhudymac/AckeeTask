package cz.ackee.testtask.characters.presentation.search

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
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

class SearchViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private val characterRepository: CharacterRepository = mock()

    private val viewModel: SearchViewModel = SearchViewModel(characterRepository)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `searchCharacters() should correctly search the characters from the repository and set the loading and characters state flows accordingly`() = runTest {
        val unconfinedTestDispatcher = UnconfinedTestDispatcher(testScheduler)

        val fakeCharacters = listOf(mockCharacter(1), mockCharacter(2))
        whenever(characterRepository.searchCharacters("Rick Sanchez")).thenReturn(fakeCharacters)

        runTest(unconfinedTestDispatcher) {
            viewModel.searchCharacters("Rick Sanchez")
        }

        advanceUntilIdle()

        assertFalse(viewModel.loading.value)
        assertEquals(fakeCharacters, viewModel.characters.value)
    }

    @Test
    fun `getText() should return the current search text`() = runTest {
        viewModel.searchText.value = "Rick Sanchez"

        val searchText = viewModel.searchText.value

        assertEquals("Rick Sanchez", searchText)
    }

    @Test
    fun `clearText() should clear the current search text`() = runTest {
        viewModel.searchText.value = "Rick Sanchez"

        viewModel.clearText()

        val searchText = viewModel.searchText.value

        assertEquals("", searchText)
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
