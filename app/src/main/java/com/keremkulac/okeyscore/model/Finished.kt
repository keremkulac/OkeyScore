package com.keremkulac.okeyscore.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "finished")
data class Finished(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val team1Name : String,
    val team2Name : String,
    val team1AllScores : List<String?>?,
    val team2AllScores : List<String?>?,
    val team1TotalScore : String,
    val team2TotalScore : String,
    val gameInfo : String,
    val date : String
)