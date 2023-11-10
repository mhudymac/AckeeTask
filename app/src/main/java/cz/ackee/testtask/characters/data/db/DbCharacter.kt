package cz.ackee.testtask.characters.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class DbCharacter(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: String,
    val location: String,
    val image: String,
    val favorite: Boolean,
)
