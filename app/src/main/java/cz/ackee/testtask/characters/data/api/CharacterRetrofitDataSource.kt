package cz.ackee.testtask.characters.data.api

import cz.ackee.testtask.characters.data.CharacterRemoteDataSource
import cz.ackee.testtask.characters.domain.Character

class CharacterRetrofitDataSource(
    private val apiDescription: CharactersApiDescription
) : CharacterRemoteDataSource {

    override suspend fun getCharacters(): List<Character> {
        return apiDescription.getCharacters().results.map { it.toCharacter() }
    }

    override suspend fun searchCharacters(name: String): List<Character> {
        return apiDescription.searchCharacters(name).results.map { it.toCharacter() }
    }

    override suspend fun getCharacter(id: Int): Character {
        return apiDescription.getCharacter(id).toCharacter()
    }

    private fun ApiCharacter.toCharacter(): Character {
        return Character(id, name, status, species, type, gender, origin.name, location.name, image)
    }
}
