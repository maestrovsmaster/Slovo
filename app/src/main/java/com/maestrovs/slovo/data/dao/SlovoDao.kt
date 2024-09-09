package com.maestrovs.slovo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SlovoDao {
    @Query("SELECT * FROM slovo")
    suspend fun getAll(): List<Slovo>

    @Query("SELECT COUNT(*) FROM slovo")
    suspend fun getGamesCount(): Int

    @Query("SELECT * FROM slovo WHERE slovo = :slovo")
    suspend fun checkSlovo(slovo: String): List<Slovo>

    @Insert
    suspend fun insertAll(vararg slovo: Slovo)


    @Query("""
        SELECT * FROM Slovo 
        WHERE level = :level 
        AND slovo NOT IN (
            SELECT slovo FROM Game 
            WHERE slovo IS NOT NULL 
            AND (result = 'WIN' OR result IS NULL)
        )
        ORDER BY RANDOM() 
        LIMIT 1
    """)
    suspend fun getRandomSlovoExcludingGame(level: Int): Slovo?

}