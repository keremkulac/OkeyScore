package com.keremkulac.okeyscore.data.repository

import com.keremkulac.okeyscore.data.local.dao.OkeyScoreDao
import com.keremkulac.okeyscore.domain.repository.OkeyScoreRepository
import com.keremkulac.okeyscore.model.FinishedPartnerGame
import com.keremkulac.okeyscore.model.FinishedSingleGame
import javax.inject.Inject

class OkeyScoreRepositoryImp @Inject constructor(private val okeyScoreDao: OkeyScoreDao) : OkeyScoreRepository {
    override suspend fun insertFinishedPartnerGame(finishedPartnerGame: FinishedPartnerGame) {
        okeyScoreDao.insertFinishedPartnerGame(finishedPartnerGame)
    }

    override suspend fun getFinishedPartnerGame(id: Int): FinishedPartnerGame {
        return okeyScoreDao.getFinishedPartnerGameById(id)
    }

    override suspend fun getAllFinishedPartnerGames(): List<FinishedPartnerGame> {
        return okeyScoreDao.getAllFinishedPartnerGames()
    }

    override suspend fun deleteFinishedPartnerGame(finishedPartnerGame: FinishedPartnerGame) {
        okeyScoreDao.deleteFinishedPartnerGame(finishedPartnerGame)
    }

    override suspend fun insertFinishedSingleGame(finishedSingleGame: FinishedSingleGame) {
        okeyScoreDao.insertFinishedSingleGame(finishedSingleGame)
    }

    override suspend fun getAllFinishedSingleGames(): List<FinishedSingleGame> {
        return okeyScoreDao.getAllFinishedSingleGames()
    }

    override suspend fun getFinishedSingleGame(id: Int): FinishedSingleGame {
        return getFinishedSingleGame(id)
    }

    override suspend fun deleteFinishedSingleGame(finishedSingleGame: FinishedSingleGame) {
        okeyScoreDao.deleteFinishedSingleGame(finishedSingleGame)
    }

}