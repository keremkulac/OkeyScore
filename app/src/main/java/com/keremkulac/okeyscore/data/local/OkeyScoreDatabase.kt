package com.keremkulac.okeyscore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.keremkulac.okeyscore.util.Converters
import com.keremkulac.okeyscore.data.local.dao.OkeyScoreDao
import com.keremkulac.okeyscore.model.Finished

@Database(entities = [Finished::class], version = 1)
@TypeConverters(Converters::class)
abstract class OkeyScoreDatabase : RoomDatabase() {
    abstract fun okeyScoreDao(): OkeyScoreDao
}
