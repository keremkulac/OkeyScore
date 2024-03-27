package com.keremkulac.okeyscore.domain.repository

import com.keremkulac.okeyscore.model.FinishedPartnerGame
import com.keremkulac.okeyscore.model.FinishedSingleGame

interface OkeyScoreRepository {

    suspend fun insertFinishedPartnerGame(finishedPartnerGame: FinishedPartnerGame)

    suspend fun getFinishedPartnerGame(id : Int) : FinishedPartnerGame

    suspend fun getAllFinishedPartnerGames() : List<FinishedPartnerGame>

    suspend fun deleteFinishedPartnerGame(finishedPartnerGame: FinishedPartnerGame)

    suspend fun insertFinishedSingleGame(finishedSingleGame: FinishedSingleGame)

    suspend fun getAllFinishedSingleGames() : List<FinishedSingleGame>

    suspend fun getFinishedSingleGame(id : Int) : FinishedSingleGame

    suspend fun deleteFinishedSingleGame(finishedSingleGame: FinishedSingleGame)

}