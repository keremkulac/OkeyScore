package com.keremkulac.okeyscore.model
import androidx.room.*

@Entity(tableName = "continuing")
data class Continuing(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val continuingTeam1: List<Int?>,
    val continuingTeam2: List<Int?>
)

