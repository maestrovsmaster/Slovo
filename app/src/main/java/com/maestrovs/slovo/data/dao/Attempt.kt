package com.maestrovs.slovo.data.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Game::class,
        parentColumns = ["slovo"],
        childColumns = ["slovoId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Attempt(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "slovoId") val slovoId: String,
    @ColumnInfo(name = "step") val step: Int,
    @ColumnInfo(name = "attempt") val attempt: String?
)