package com.maestrovs.slovo.data.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Slovo(
    @ColumnInfo(name = "slovo") val slovo: String,
    @ColumnInfo(name = "level") val level: Int?,
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
