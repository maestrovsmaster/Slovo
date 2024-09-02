package com.maestrovs.slovo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SlovoDao {
    @Query("SELECT * FROM slovo")
    fun getAll(): List<Slovo>

    @Query("SELECT COUNT(*) FROM slovo")
    fun getGamesCount(): Int

    @Query("SELECT * FROM slovo WHERE slovo.step = 0 ORDER BY RANDOM() LIMIT 1")
    fun getRandomSlovo():List<Slovo>


    @Query("UPDATE slovo SET step=:step WHERE slovo = :slovo")
    fun updateSlovoStep(slovo: String, step: Int)

    @Query("SELECT * FROM slovo WHERE step != 0")
    fun getPlayedGames(): List<Slovo>

    @Query("SELECT * FROM slovo WHERE slovo = :slovo")
    fun checkSlovo(slovo: String): List<Slovo>

  //  @Query("SELECT * FROM game WHERE uid IN (:userIds)")
 //   fun loadAllByIds(userIds: IntArray): List<Game>

    //@Query("SELECT * FROM game WHERE first_name LIKE :first AND " +
   //         "last_name LIKE :last LIMIT 1")
   // fun findByName(first: String, last: String): Game

    @Insert
    fun insertAll(vararg slovo: Slovo)



   // @Delete
   // fun delete(game: Game)

}