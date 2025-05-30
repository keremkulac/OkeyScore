package com.keremkulac.okeyscore.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "finishedPartnerGame")
data class FinishedPartnerGame(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val team1Name : String,
    val team2Name : String,
    val team1TotalScore : Int,
    val team2TotalScore : Int,
    @Embedded(prefix = "team1Player1")
    val team1Player1 : Player?,
    @Embedded(prefix = "team1Player2")
    val team1Player2 : Player?,
    @Embedded(prefix = "team2Player1")
    val team2Player1 : Player?,
    @Embedded(prefix = "team2Player2")
    val team2Player2 : Player?,
    @Embedded(prefix = "gameInfo")  val gameInfo: Info
)