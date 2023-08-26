package com.keremkulac.okeyscore.data.repository

import com.keremkulac.okeyscore.data.local.dao.OkeyScoreDao
import com.keremkulac.okeyscore.model.Finished
import javax.inject.Inject

class OkeyScoreRepository @Inject constructor(private val okeyScoreDao: OkeyScoreDao) {



    suspend fun insertFinishedGame(finished: Finished) = okeyScoreDao.insertFinishedGame(finished)

    suspend fun getFinishedGame(id : Int) = okeyScoreDao.getFinishedGameById(id)

    suspend fun getAllFinishedGames() = okeyScoreDao.getAllFinishedGames()

}