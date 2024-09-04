package com.maestrovs.slovo.data.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["slovo"], unique = true)]
)
data class Game(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "slovo") val slovo: String,
    @ColumnInfo(name = "level") val level: Int?,
    @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "result") val result: String?
)


