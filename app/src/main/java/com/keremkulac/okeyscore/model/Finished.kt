package com.keremkulac.okeyscore.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "finished")
data class Finished(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    @Embedded(prefix = "team1") val team1 : Player?,
    @Embedded(prefix = "team2")  val team2 : Player?,
    @Embedded(prefix = "gameInfo")  val gameInfo: Info
)