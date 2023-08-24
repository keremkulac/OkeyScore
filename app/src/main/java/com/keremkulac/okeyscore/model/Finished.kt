package com.keremkulac.okeyscore.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "finished")
data class Finished(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val redTeamName : String,
    val blueTeamName : String,
    val finishedRedTeam: List<String?>?,
    val finishedBlueTeam: List<String?>?,
    val redTeamTotalScore : Int,
    val blueTeamTotalScore : Int
)