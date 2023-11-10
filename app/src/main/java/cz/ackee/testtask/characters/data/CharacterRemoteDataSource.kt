package cz.ackee.testtask.characters.data

import cz.ackee.testtask.characters.domain.Character

interface CharacterRemoteDataSource {
    suspend fun getCharacters(page: Int): List<Character>

    suspend fun searchCharacters(name: String, page: Int): List<Character>

    suspend fun getCharacter(id: Int): Character
}
