package cz.ackee.testtask.characters.data.db

import cz.ackee.testtask.characters.data.CharacterLocalDataSource
import cz.ackee.testtask.characters.domain.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CharacterRoomDataSource(private val characterDao: CharacterDao) : CharacterLocalDataSource {

    override suspend fun getCharacter(id: Int): Character? {
        return characterDao.getCharacter(id)?.toCharacter()
    }

    override suspend fun isFavorite(id: Int): Boolean {
        return characterDao.getCharacter(id) != null
    }

    override suspend fun getFavorites(): Flow<List<Character>> {
        return characterDao.getFavorites().map {
            it.map { dbCharacter -> dbCharacter.toCharacter() }
        }
    }

    override suspend fun insertCharacter(character: Character) {
        characterDao.insertCharacter(character.toDb())
    }

    override suspend fun deleteCharacters() {
        characterDao.deleteCharacters()
    }

    override suspend fun deleteCharacter(character: Character) {
        characterDao.deleteCharacter(character.toDb())
    }

    private fun DbCharacter.toCharacter(): Character {
        return Character(id, name, status, species, type, gender, origin, location, image, favorite)
    }
    private fun Character.toDb(): DbCharacter {
        return DbCharacter(id, name, status, species, type, gender, origin, location, image, favorite)
    }
}
