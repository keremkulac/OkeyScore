package com.keremkulac.okeyscore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.keremkulac.okeyscore.data.local.dao.OkeyScoreDao
import com.keremkulac.okeyscore.model.FinishedPartnerGame
import com.keremkulac.okeyscore.model.FinishedSingleGame
import com.keremkulac.okeyscore.util.IntegerListConverter
import com.keremkulac.okeyscore.util.PlayerConverter

@Database(entities = [FinishedPartnerGame::class,FinishedSingleGame::class], version = 2)
@TypeConverters(PlayerConverter::class,IntegerListConverter::class)
abstract class OkeyScoreDatabase : RoomDatabase() {
    abstract fun okeyScoreDao(): OkeyScoreDao
}
