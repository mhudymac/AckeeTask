package cz.ackee.testtask.characters.presentation.search

import cz.ackee.testtask.characters.MainCoroutineRule
import cz.ackee.testtask.characters.data.CharacterRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule(UnconfinedTestDispatcher())

    private lateinit var classUnderTest: SearchViewModel

    @Mock
    private lateinit var characterRepository: CharacterRepository

    @Before
    fun setUp() {
        classUnderTest = SearchViewModel(characterRepository)
    }

    @Test
    fun `Given name is set when searchCharacters then should change searchText`() = runTest {
        // Given
        val name = "rick"

        // When
        classUnderTest.searchCharacters(name)

        // Then
        assertEquals(classUnderTest.searchText.value, name)
    }

    @Test
    fun `When clearText then set searchText to ""`() {
        // When
        classUnderTest.clearText()

        // Then
        assertEquals(classUnderTest.searchText, "")
    }

}
