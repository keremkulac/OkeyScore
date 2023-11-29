package com.keremkulac.okeyscore.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "finishedSingleGame")
data class FinishedSingleGame(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    @Embedded(prefix = "player1") val player1 : Player?,
    @Embedded(prefix = "player2")  val player2 : Player?,
    @Embedded(prefix = "player3")  val player3 : Player?,
    @Embedded(prefix = "player4")  val player4 : Player?,
    @Embedded(prefix = "gameInfo")  val gameInfo: Info
)
