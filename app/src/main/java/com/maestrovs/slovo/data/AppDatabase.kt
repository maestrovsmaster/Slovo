package com.maestrovs.slovo.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Slovo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}