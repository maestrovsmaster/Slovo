package com.maestrovs.slovo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AttemptDao {
    @Insert
     fun insertAttempt(attempt: Attempt): Long



    @Query("""
        SELECT * FROM Attempt 
        WHERE slovoId = :slovoId
        ORDER BY id ASC
    """)
    suspend fun getAttemptsForGame(slovoId: String): List<Attempt>
}