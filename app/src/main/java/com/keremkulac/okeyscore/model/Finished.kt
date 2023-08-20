package com.keremkulac.okeyscore.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "finished")
data class Finished(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val finishedTeam1: List<Int?>,
    val finishedTeam2: List<Int?>
)