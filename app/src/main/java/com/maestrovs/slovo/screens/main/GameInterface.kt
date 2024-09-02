package com.maestrovs.slovo.screens.main

import com.maestrovs.slovo.model.Key

interface GameInterface {

    /**
     * Must clear and to add binding words
     */
    fun initRows(rows: MutableList<com.maestrovs.slovo.components.UITilesRow>): MutableList<com.maestrovs.slovo.components.UITilesRow>


    fun updateKeyboard(keyboardKeys: MutableList<Key>)

    fun showWrongWordAnimation()

    fun resetGame()

    fun showWin()

    fun showFail()

    fun showNotComplete()



}