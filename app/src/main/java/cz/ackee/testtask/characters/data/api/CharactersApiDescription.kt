package cz.ackee.testtask.characters.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharactersApiDescription {

    @GET("character/?page=1")
    suspend fun getCharacters(): CharactersResponse

    @GET("character/")
    suspend fun searchCharacters(@Query("name") name: String): CharactersResponse

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): ApiCharacter
}
