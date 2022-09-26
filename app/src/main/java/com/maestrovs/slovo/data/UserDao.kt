package com.maestrovs.slovo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM game")
    fun getAll(): List<Game>

    @Query("SELECT COUNT(*) FROM game")
    fun getGamesCount(): Int

    @Query("SELECT * FROM game WHERE game.step = 0 ORDER BY RANDOM() LIMIT 1")
    fun getRandomSlovo():List<Game>


    @Query("UPDATE game SET step=:step WHERE slovo = :slovo")
    fun updateSlovoStep(slovo: String, step: Int)

    @Query("SELECT * FROM game WHERE step != 0")
    fun getPlayedGames(): List<Game>

    @Query("SELECT * FROM game WHERE slovo = :slovo")
    fun checkSlovo(slovo: String): List<Game>

  //  @Query("SELECT * FROM game WHERE uid IN (:userIds)")
 //   fun loadAllByIds(userIds: IntArray): List<Game>

    //@Query("SELECT * FROM game WHERE first_name LIKE :first AND " +
   //         "last_name LIKE :last LIMIT 1")
   // fun findByName(first: String, last: String): Game

    @Insert
    fun insertAll(vararg game: Game)



   // @Delete
   // fun delete(game: Game)

}