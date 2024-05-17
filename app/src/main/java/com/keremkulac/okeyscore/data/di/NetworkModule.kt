package com.keremkulac.okeyscore.data.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.keremkulac.okeyscore.data.local.Migration_1_to_2
import com.keremkulac.okeyscore.data.local.dao.OkeyScoreDao
import com.keremkulac.okeyscore.data.local.OkeyScoreDatabase
import com.keremkulac.okeyscore.data.repository.OkeyScoreRepositoryImp
import com.keremkulac.okeyscore.domain.repository.OkeyScoreRepository
import com.keremkulac.okeyscore.presentation.ui.finishedPartnerGame.FinishedPartnerGameAdapter
import com.keremkulac.okeyscore.presentation.ui.finishedSingleGame.FinishedSingleGameAdapter
import com.keremkulac.okeyscore.util.SharedPrefHelper
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
        )
            .addMigrations(Migration_1_to_2())
            .build()

        //            .fallbackToDestructiveMigration()
        //            .addMigrations(Migration_1_to_2())
    }

    @Provides
    @Singleton
    fun provideDao(okeyScoreDatabase: OkeyScoreDatabase) : OkeyScoreDao {
        return okeyScoreDatabase.okeyScoreDao()
    }

    @Provides
    @Singleton
    fun provideFinishedSingleGameAdapter() : FinishedSingleGameAdapter{
        return FinishedSingleGameAdapter()
    }

    @Provides
    @Singleton
    fun provideFinishedPartnerGameAdapter() : FinishedPartnerGameAdapter{
        return FinishedPartnerGameAdapter()
    }
    @Provides
    @Singleton
    fun provideOkeyRepository(okeyScoreDao: OkeyScoreDao) : OkeyScoreRepository{
        return OkeyScoreRepositoryImp(okeyScoreDao)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    object AppModule {
        @Provides
        fun provideSharedPrefHelper(context: Application): SharedPrefHelper {
            return SharedPrefHelper(context)
        }
    }


}