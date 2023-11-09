package cz.ackee.testtask.characters.di

import cz.ackee.testtask.characters.data.CharacterLocalDataSource
import cz.ackee.testtask.characters.data.CharacterRemoteDataSource
import cz.ackee.testtask.characters.data.CharacterListPagingDataSource
import cz.ackee.testtask.characters.data.CharacterRepository
import cz.ackee.testtask.characters.data.api.CharacterRetrofitDataSource
import cz.ackee.testtask.characters.data.api.CharactersApiDescription
import cz.ackee.testtask.characters.data.db.CharacterRoomDataSource
import cz.ackee.testtask.characters.presentation.detail.DetailViewModel
import cz.ackee.testtask.characters.presentation.list.ListViewModel
import cz.ackee.testtask.characters.presentation.list.FavoriteViewModel
import cz.ackee.testtask.characters.presentation.search.SearchViewModel
import cz.ackee.testtask.core.data.db.CharacterDatabase
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import retrofit2.Retrofit

val characterModule get() = module {
    single { get<Retrofit>().create(CharactersApiDescription::class.java) }
    factory<CharacterRemoteDataSource> { CharacterRetrofitDataSource(apiDescription = get()) }

    single { get<CharacterDatabase>().characterDao() }
    factory<CharacterLocalDataSource> { CharacterRoomDataSource(characterDao = get()) }

    factory { CharacterListPagingDataSource(remoteDataSource = get(), localDataSource = get()) }

    factoryOf(::CharacterRepository)

    viewModelOf(::DetailViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::ListViewModel)
    viewModelOf(::FavoriteViewModel)
}
