package com.maestrovs.slovo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maestrovs.slovo.model.GameResult

@Dao
interface GameDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGame(game: Game): Long

    @Query("SELECT * FROM Game WHERE id = :id")
    suspend fun getGameById(id: Int): Game?

    @Query("SELECT * FROM Game")
    suspend fun getAllGames(): List<Game>

    @Query("UPDATE Game SET result=:result WHERE slovo = :slovo")
    suspend fun updateGameResult(slovo: String, result: String)

    @Query("""
        SELECT * FROM Game 
        WHERE result = 'NONE'
        ORDER BY id DESC 
        LIMIT 1
    """)
    suspend fun getLastUnfinishedGame(): Game?

}