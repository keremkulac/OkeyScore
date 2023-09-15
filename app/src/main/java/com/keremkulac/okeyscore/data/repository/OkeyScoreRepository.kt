package com.keremkulac.okeyscore.data.repository

import com.keremkulac.okeyscore.util.SharedPreferencesManager
import com.keremkulac.okeyscore.data.local.dao.OkeyScoreDao
import com.keremkulac.okeyscore.model.Finished
import javax.inject.Inject

class OkeyScoreRepository @Inject constructor(private val okeyScoreDao: OkeyScoreDao,
                                              private val sharedPreferencesManager: SharedPreferencesManager
) {



    suspend fun insertFinishedGame(finished: Finished) {
        sharedPreferencesManager.clearStoredData()
        okeyScoreDao.insertFinishedGame(finished)
    }

    suspend fun getFinishedGame(id : Int) = okeyScoreDao.getFinishedGameById(id)

    suspend fun getAllFinishedGames() = okeyScoreDao.getAllFinishedGames()

    suspend fun deleteFinishedGame(finished: Finished?) = okeyScoreDao.deleteFinishedGame(finished)


}