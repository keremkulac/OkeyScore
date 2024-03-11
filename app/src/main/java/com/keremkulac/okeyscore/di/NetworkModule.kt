package com.keremkulac.okeyscore.di

import android.content.Context
import androidx.room.Room
import com.keremkulac.okeyscore.data.local.dao.OkeyScoreDao
import com.keremkulac.okeyscore.data.local.OkeyScoreDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext appContext: Context): OkeyScoreDatabase {
        return Room.databaseBuilder(
            appContext,
            OkeyScoreDatabase::class.java,
            "okeyScore.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(okeyScoreDatabase: OkeyScoreDatabase) : OkeyScoreDao {
        return okeyScoreDatabase.okeyScoreDao()
    }

}