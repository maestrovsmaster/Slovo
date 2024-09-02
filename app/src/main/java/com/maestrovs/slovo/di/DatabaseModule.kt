package com.maestrovs.slovo.di

import android.content.Context
import androidx.room.Room
import com.maestrovs.slovo.data.dao.AppDatabase
import com.maestrovs.slovo.data.dao.AttemptDao
import com.maestrovs.slovo.data.dao.GameDao
import com.maestrovs.slovo.data.dao.SlovoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "database-name"
        ).build()
    }

    @Provides
    fun provideSlovoDao(appDatabase: AppDatabase): SlovoDao {
        return appDatabase.slovoDao()
    }

    @Provides
    fun provideGameDao(appDatabase: AppDatabase): GameDao {
        return appDatabase.gameDao()
    }

    @Provides
    fun provideAttemptDao(appDatabase: AppDatabase): AttemptDao {
        return appDatabase.attemptDao()
    }
}