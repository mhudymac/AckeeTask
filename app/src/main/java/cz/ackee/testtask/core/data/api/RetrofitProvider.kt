package cz.ackee.testtask.core.data.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitProvider {
    private val json = Json { ignoreUnknownKeys = true }
    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    @OptIn(ExperimentalSerializationApi::class)
    fun provide(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
}
