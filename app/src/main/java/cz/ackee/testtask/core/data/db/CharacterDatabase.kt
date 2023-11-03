package cz.ackee.testtask.core.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cz.ackee.testtask.characters.data.db.CharacterDao
import cz.ackee.testtask.characters.data.db.DbCharacter

@Database(version = 1, entities = [DbCharacter::class])
abstract class CharacterDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    companion object {
        fun instance(context: Context): CharacterDatabase {
            return Room.databaseBuilder(context, CharacterDatabase::class.java, "characters.db").build()
        }
    }
}
