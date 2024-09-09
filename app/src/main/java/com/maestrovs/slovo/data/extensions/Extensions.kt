package com.maestrovs.slovo.data.extensions

import com.maestrovs.slovo.data.dao.Game
import com.maestrovs.slovo.data.dao.Slovo

fun Game.toSlovo() = Slovo(this.slovo, this.level)