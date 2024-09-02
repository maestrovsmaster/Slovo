package com.maestrovs.slovo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AttemptDao {
    @Insert
    suspend fun insertAttempt(attempt: Attempt): Long

    @Query("SELECT * FROM Attempt WHERE gameId = :gameId")
    suspend fun getAttemptsForGame(gameId: Int): List<Attempt>
}