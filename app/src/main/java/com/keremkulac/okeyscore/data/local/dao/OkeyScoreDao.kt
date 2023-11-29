package com.keremkulac.okeyscore.data.local.dao

import androidx.room.*
import com.keremkulac.okeyscore.model.Finished

@Dao
interface OkeyScoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinishedGame(finished: Finished)

    @Query("SELECT * FROM finished WHERE id = :id")
    suspend fun getFinishedGameById(id: Int): Finished?

    @Query("SELECT * FROM finished ORDER BY gameInfoDate DESC")
    suspend fun getAllFinishedGames(): List<Finished>

    @Delete
    suspend fun deleteFinishedGame(finished : Finished?)
}