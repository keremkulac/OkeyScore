package com.keremkulac.okeyscore.data.repository

import com.keremkulac.okeyscore.data.local.dao.OkeyScoreDao
import com.keremkulac.okeyscore.model.FinishedPartnerGame
import com.keremkulac.okeyscore.model.FinishedSingleGame
import javax.inject.Inject

class OkeyScoreRepository @Inject constructor(private val okeyScoreDao: OkeyScoreDao) {

    suspend fun insertFinishedPartnerGame(finishedPartnerGame: FinishedPartnerGame) {
        okeyScoreDao.insertFinishedPartnerGame(finishedPartnerGame)
    }

    suspend fun getFinishedPartnerGame(id : Int) = okeyScoreDao.getFinishedPartnerGameById(id)

    suspend fun getAllFinishedPartnerGames() = okeyScoreDao.getAllFinishedPartnerGames()

    suspend fun deleteFinishedPartnerGame(finishedPartnerGame: FinishedPartnerGame) = okeyScoreDao.deleteFinishedPartnerGame(finishedPartnerGame)

    suspend fun insertFinishedSingleGame(finishedSingleGame: FinishedSingleGame) = okeyScoreDao.insertFinishedSingleGame(finishedSingleGame)

    suspend fun getAllFinishedSingleGames() = okeyScoreDao.getAllFinishedSingleGames()

    suspend fun getFinishedSingleGame(id : Int) = okeyScoreDao.getFinishedSingleGameById(id)

    suspend fun deleteFinishedSingleGame(finishedSingleGame: FinishedSingleGame) = okeyScoreDao.deleteFinishedSingleGame(finishedSingleGame)

}