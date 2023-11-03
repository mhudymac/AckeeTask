package cz.ackee.testtask.characters.presentation.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import cz.ackee.testtask.characters.data.CharacterRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
class ListViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val characterRepository: CharacterRepository = mock()
    private val viewModel: ListViewModel = ListViewModel(characterRepository)

    @Test
    fun `on init should call loadCharacters() and set screenFavorite to false`() = runTest {
        assertFalse(viewModel.state.value.screenFavorite)
        verify(characterRepository).loadCharacters()
    }
}
