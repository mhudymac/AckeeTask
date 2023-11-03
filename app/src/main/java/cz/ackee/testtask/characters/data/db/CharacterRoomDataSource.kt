package cz.ackee.testtask.characters.data.db

import cz.ackee.testtask.characters.data.CharacterLocalDataSource
import cz.ackee.testtask.characters.domain.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CharacterRoomDataSource(private val characterDao: CharacterDao) : CharacterLocalDataSource {
    override suspend fun getAllCharacters(): Flow<List<Character>> {
        return characterDao.getAllCharacters().map {
            it.map { dbCharacter -> dbCharacter.toCharacter() }
        }
    }

    override suspend fun getCharacter(id: Int): Character? {
        return characterDao.getCharacter(id)?.toCharacter()
    }

    override suspend fun getFavorites(): Flow<List<Character>> {
        return characterDao.getFavorites().map {
            it.map { dbCharacter -> dbCharacter.toCharacter() }
        }
    }

    override suspend fun insertCharacters(characters: List<Character>) {
        characterDao.insertCharacters(characters.map { it.toDb() })
    }

    override suspend fun deleteCharacters() {
        characterDao.deleteCharacters()
    }

    override suspend fun updateCharacter(character: Character) {
        characterDao.updateCharacter(character.toDb())
    }

    private fun DbCharacter.toCharacter(): Character {
        return Character(id, name, status, species, type, gender, origin, location, image, favorite)
    }
    private fun Character.toDb(): DbCharacter {
        return DbCharacter(id, name, status, species, type, gender, origin, location, image, favorite)
    }
}
