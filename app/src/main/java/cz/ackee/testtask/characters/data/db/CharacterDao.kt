package cz.ackee.testtask.characters.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters")
    fun getAllCharacters(): Flow<List<DbCharacter>>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharacter(id: Int): DbCharacter?

    @Query("SELECT * FROM characters WHERE favorite = 1")
    fun getFavorites(): Flow<List<DbCharacter>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCharacters(characters: List<DbCharacter>)

    @Query("DELETE FROM characters")
    suspend fun deleteCharacters()

    @Update
    suspend fun updateCharacter(character: DbCharacter)
}
