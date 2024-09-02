package com.maestrovs.slovo.data.dao

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Slovo::class, Game::class, Attempt::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun slovoDao(): SlovoDao
    abstract fun gameDao(): GameDao
    abstract fun attemptDao(): AttemptDao
}