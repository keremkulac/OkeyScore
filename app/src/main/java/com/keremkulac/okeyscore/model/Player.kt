package com.keremkulac.okeyscore.model

data class Player(
    val id : Int,
    val name : String,
    val allScores : List<String?>?,
    val totalScore : String,
    val penalties : List<Int?>
)
