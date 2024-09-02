package com.maestrovs.slovo.data.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Game(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "slovo") val slovo: String?,
    @ColumnInfo(name = "date") val date: String?
)
