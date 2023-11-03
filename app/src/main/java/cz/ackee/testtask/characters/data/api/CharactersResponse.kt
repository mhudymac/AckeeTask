package cz.ackee.testtask.characters.data.api

import kotlinx.serialization.Serializable

@Serializable
data class CharactersResponse(val results: List<ApiCharacter>)
