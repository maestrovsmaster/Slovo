package com.maestrovs.slovo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Game(
    @ColumnInfo(name = "slovo") val slovo: String?,
    @ColumnInfo(name = "step") val step: Int?,
    @ColumnInfo(name = "num") val num: Int?,
    @ColumnInfo(name = "result") val result: Boolean?
){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
