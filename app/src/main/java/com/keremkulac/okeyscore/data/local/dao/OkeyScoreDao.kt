package com.keremkulac.okeyscore.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.keremkulac.okeyscore.model.Continuing
import com.keremkulac.okeyscore.model.Finished

@Dao
interface OkeyScoreDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContinuingGame(continuing: Continuing)

    @Query("SELECT * FROM continuing WHERE id = :id")
    suspend fun getContinuingGameById(id: Int): Continuing?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinishedGame(finished: Finished)

    @Query("SELECT * FROM finished WHERE id = :id")
    suspend fun getFinishedGameById(id: Int): Finished?
}