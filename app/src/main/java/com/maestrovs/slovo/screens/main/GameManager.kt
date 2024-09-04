package com.maestrovs.slovo.screens.main

import android.content.Context
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.maestrovs.slovo.R
import com.maestrovs.slovo.components.OpenStatus
import com.maestrovs.slovo.data.dao.Slovo
import com.maestrovs.slovo.model.GameResult
import com.maestrovs.slovo.model.Key
import com.maestrovs.slovo.model.KeyType
import com.maestrovs.slovo.model.KeyUI
import com.maestrovs.slovo.model.isMoreWeight

class GameManager(
    private val context: Context,
    private val gameViewModel: GameViewModel,
    private val gameInterface: GameInterface
) {

    private var _slovo = Slovo("ПОШТА",1)
    val slovo: Slovo
        get() = _slovo

    private var gameNum = 0
    private var step = 0
    private var lastStep = 5

    private var rows: MutableList<com.maestrovs.slovo.components.UITilesRow> = mutableListOf()
    private val keyboardKeys: MutableList<Key> = mutableListOf()
    private var currentRow: com.maestrovs.slovo.components.UITilesRow? = null


    fun init() {
        keyboardKeys.clear()
        keyboardKeys.clear()
        initRows()
    }

    fun nextGame() {

        gameViewModel.selectRandomSlovo { sl, savedSteps ->
            _slovo = sl
            Log.d("Game", "RandomSlovo = $sl")
            Log.d("Game", "SavedSteps = $savedSteps")
            //

            resetGame()
            gameViewModel.registerGameToDB(slovo)
            currentRow!!.slovo = slovo.slovo
            gameNum += 1
            if (!savedSteps.isNullOrEmpty()) {
                restoreGame(savedSteps)

            }
        }
    }


    public fun setKeyUI(keyUI: KeyUI) {
        when (keyUI.type) {
            KeyType.Letter -> currentRow?.addKey(keyUI.key)
            KeyType.Enter -> {
                val usersEntered = currentRow?.getUserWord() ?: ""
                if(currentRow?.isComplete() == true) {
                    gameViewModel.checkSlovo(usersEntered) { correct ->
                        if (correct) {
                            val status = currentRow?.openSlovo()
                            processGameStatus(status!!, usersEntered)
                        } else {
                            showWrongWord()
                        }
                    }
                }else{
                    showNotCompleteWord()
                }
            }

            KeyType.Backspace -> {
                currentRow?.removeLast()
            }
        }
    }


    private fun initRows() {
        rows = gameInterface.initRows(rows)
    }


    private fun processGameStatus(status: OpenStatus, userEntered: String? = null) {

        Log.d(
            "Game",
            "status =$status   lastWord = $userEntered   slovo=$slovo step =$step lastStep = $lastStep"
        )

        when (status) {
            OpenStatus.Win, OpenStatus.Game ->{
                userEntered?.let {
                    gameViewModel.writeAttemptToDB(slovo.slovo, step = step, word = userEntered)
                }
            }

            else -> {}
        }

        when (status) {



            OpenStatus.Win -> {
                gameViewModel.updateGameToDB(slovo, GameResult.WIN)
                showWin()
            }

            OpenStatus.Game -> {

                val nextKeys = currentRow?.getKeyboardActualKeys()
                if (canNext()) {

                    nextStep()
                    addKeysToExists(nextKeys ?: mutableListOf())

                    gameInterface.updateKeyboard(keyboardKeys)
                } else {
                    //Fail
                    if (step >= 0) {
                        step = -1 //Можна повторну спробу вгадати слово
                    } else {
                        step = -2 //Немає більше спроб вгадати слово
                    }
                    //gameViewModel.updateSlovoStepInDB(Slovo(slovo, null, step, 0, false))
                    gameViewModel.updateGameToDB(slovo, GameResult.LOSE)

                    showFail()
                }
            }

            OpenStatus.WrongWord -> {
                //покажем анімашку, що хибне слово
                currentRow?.startAnimation(
                    AnimationUtils.loadAnimation(
                        context,
                        R.anim.shake
                    )
                )

                showWrongWord()
            }

            OpenStatus.NoComplete -> {
                showNotCompleteWord()
            }
        }
    }

    private fun showNotCompleteWord(){
        currentRow?.showNotCompleteTiles()
        gameInterface.showNotComplete()
    }

    private fun showWrongWord(){
        currentRow?.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.shake
            )
        )
        gameInterface.showWrongWordAnimation()
    }

    private fun addKeysToExists(adds: List<Key>) {


        adds.map { letter ->
            var curentLetter = letter
            val sameLs = adds.filter { ds -> ds.value == letter.value }
            sameLs.map { same ->
                if (same.state.isMoreWeight(curentLetter.state)) {
                    curentLetter = same
                }
            }

            if (!keyboardKeys.contains(curentLetter)) {
                keyboardKeys.add(curentLetter)
            } else {
                var curentLetter2 = curentLetter
                val sameLs = keyboardKeys.filter { ds -> ds.value == curentLetter.value }
                if (sameLs.isNotEmpty()) {
                    if (sameLs[0].state.isMoreWeight(curentLetter2.state)) {
                        curentLetter2 = sameLs[0]
                    } else {
                    }
                } else {
                    //Nothing
                }
                keyboardKeys.add(curentLetter2)
            }
        }
    }


    private fun restoreGame(words: List<String>) {
        resetGame()
        currentRow!!.slovo = slovo.slovo
        words.map {
            val status = currentRow!!.forceOpenSlovo(it)
            processGameStatus(status)
        }
    }


    private fun nextStep() {

        step += 1
        currentRow = rows[step]
        currentRow!!.slovo = slovo.slovo


    }

    private fun canNext(): Boolean {
        return step < lastStep
    }

    private fun resetGame() {

        gameInterface.resetGame()

        step = 0

       // gameViewModel.resetSavedGame()
        keyboardKeys.clear()

        rows.map {
            it.slovo = ""
            it.clearSlovo()
        }
        currentRow = rows[0]
    }


    private fun showWin() {

        gameInterface.showWin()

    }

    private fun showFail() {

       // gameViewModel.resetSavedGame()

        gameInterface.showFail()


    }


}