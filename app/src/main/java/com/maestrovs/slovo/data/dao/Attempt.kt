package com.maestrovs.slovo.data.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Game::class,
        parentColumns = ["id"],
        childColumns = ["gameId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Attempt(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "gameId") val gameId: Int,
    @ColumnInfo(name = "attemptId") val attemptId: Int,
    @ColumnInfo(name = "attempt") val attempt: String?
)