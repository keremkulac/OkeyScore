package com.keremkulac.okeyscore.data.local.dao

import androidx.room.*
import com.keremkulac.okeyscore.model.FinishedPartnerGame

@Dao
interface OkeyScoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinishedGame(finishedPartnerGame: FinishedPartnerGame)

    @Query("SELECT * FROM finishedPartnerGame WHERE id = :id")
    suspend fun getFinishedGameById(id: Int): FinishedPartnerGame?

    @Query("SELECT * FROM finishedPartnerGame ORDER BY gameInfoDate DESC")
    suspend fun getAllFinishedGames(): List<FinishedPartnerGame>

    @Delete
    suspend fun deleteFinishedGame(finishedPartnerGame : FinishedPartnerGame?)
}