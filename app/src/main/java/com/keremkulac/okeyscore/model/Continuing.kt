package com.keremkulac.okeyscore.model

data class Continuing(
    val id: Int = 0,
    val continuingRedTeam: List<Int?>,
    val continuingBlueTeam: List<Int?>
)

