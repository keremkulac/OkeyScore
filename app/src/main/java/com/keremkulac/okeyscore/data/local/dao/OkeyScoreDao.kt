package com.keremkulac.okeyscore.data.local.dao

import androidx.room.*
import com.keremkulac.okeyscore.model.FinishedPartnerGame
import com.keremkulac.okeyscore.model.FinishedSingleGame

@Dao
interface OkeyScoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinishedPartnerGame(finishedPartnerGame: FinishedPartnerGame)

    @Query("SELECT * FROM finishedPartnerGame WHERE id = :id")
    suspend fun getFinishedPartnerGameById(id: Int): FinishedPartnerGame

    @Query("SELECT * FROM finishedPartnerGame ORDER BY gameInfodate DESC")
    suspend fun getAllFinishedPartnerGames(): List<FinishedPartnerGame>

    @Delete
    suspend fun deleteFinishedPartnerGame(finishedPartnerGame : FinishedPartnerGame)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinishedSingleGame(finishedSingleGame: FinishedSingleGame)

    @Query("SELECT * FROM finishedSingleGame ORDER BY gameInfodate DESC")
    suspend fun getAllFinishedSingleGames(): List<FinishedSingleGame>

    @Query("SELECT * FROM finishedSingleGame WHERE id = :id")
    suspend fun getFinishedSingleGameById(id: Int): FinishedSingleGame

    @Delete
    suspend fun deleteFinishedSingleGame(finishedSingleGame: FinishedSingleGame)

}