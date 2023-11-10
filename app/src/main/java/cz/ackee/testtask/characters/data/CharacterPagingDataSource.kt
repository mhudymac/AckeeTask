package cz.ackee.testtask.characters.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import cz.ackee.testtask.characters.domain.Character
import retrofit2.HttpException
import java.io.IOException

class CharacterSearchPagingDataSource(private val remoteDataSource: CharacterRemoteDataSource, private val name: String = "") : PagingSource<Int, Character>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
            val currentPage = params.key ?: 1
            val characters = remoteDataSource.searchCharacters(name = name, page = currentPage)

            LoadResult.Page(
                data = characters,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = if (characters.isEmpty()) null else currentPage.plus(1),
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition
    }
}

class CharacterListPagingDataSource(private val remoteDataSource: CharacterRemoteDataSource, private val localDataSource: CharacterLocalDataSource) : PagingSource<Int, Character>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
            val currentPage = params.key ?: 1

            val characters = remoteDataSource.getCharacters(page = currentPage).map { character ->
                character.copy(favorite = localDataSource.isFavorite(character.id))
            }

            LoadResult.Page(
                data = characters,
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1),
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition
    }
}
