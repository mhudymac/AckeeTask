package cz.ackee.testtask.characters.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharacter(id: Int): DbCharacter?

    @Query("SELECT * FROM characters")
    fun getFavorites(): Flow<List<DbCharacter>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCharacter(character: DbCharacter)

    @Query("DELETE FROM characters")
    suspend fun deleteCharacters()

    @Delete
    suspend fun deleteCharacter(character: DbCharacter)
}
