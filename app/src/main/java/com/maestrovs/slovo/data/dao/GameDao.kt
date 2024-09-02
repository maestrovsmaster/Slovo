package com.maestrovs.slovo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GameDao {
    @Insert
    suspend fun insertGame(game: Game): Long

    @Query("SELECT * FROM Game WHERE id = :id")
    suspend fun getGameById(id: Int): Game?

    @Query("SELECT * FROM Game")
    suspend fun getAllGames(): List<Game>

}