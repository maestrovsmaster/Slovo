package com.maestrovs.slovo.screens.main

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.maestrovs.slovo.common.DateUtilities
import com.maestrovs.slovo.common.SharedManager
import com.maestrovs.slovo.data.dao.AppDatabase
import com.maestrovs.slovo.data.dao.Game
import com.maestrovs.slovo.data.dao.Slovo
import com.maestrovs.slovo.data.extensions.toSlovo
import com.maestrovs.slovo.model.GameResult
import com.maestrovs.slovo.screens.base.BaseViewModel
import com.maestrovs.slovo.screens.extensions.addTo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(application: Application, private val db: AppDatabase) :
    BaseViewModel(application) {

     //fun readLastSlovo() = SharedManager.readLastSlovo(context)

    fun selectRandomSlovo(callback: (Slovo, savedSteps: List<String>?) -> (Unit)) {


        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val lastGame = db.gameDao().getLastUnfinishedGame()

                if (lastGame?.slovo != null) {
                    val attempts = db.attemptDao().getAttemptsForGame(lastGame.slovo)
                    val attemptsList: List<String> = attempts.mapNotNull { it.attempt }
                    callback(lastGame.toSlovo(), attemptsList)
                } else {
                    val slovo = db.slovoDao().getRandomSlovoExcludingGame(1) ?: Slovo("ФІНІШ", 1)

                    callback(slovo, null)
                }
            }
        }
    }

    fun registerGameToDB(slovo: Slovo) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val game = Game(
                    slovo = slovo.slovo,
                    level = slovo.level,
                    date = DateUtilities.getCurrentDateTimeString(),
                    result = GameResult.NONE.name
                )
                db.gameDao().insertGame(game)
            }
        }
    }

    fun writeAttemptToDB(slovo: String, step: Int, word: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val attempt = com.maestrovs.slovo.data.dao.Attempt(
                    slovoId = slovo,
                    step = step,
                    attempt = word
                )
                db.attemptDao().insertAttempt(attempt)
            }
        }
    }

    fun updateGameToDB(slovo: Slovo, gameResult: GameResult) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                withContext(Dispatchers.Main) {
                    db.gameDao().updateGameResult(slovo.slovo, gameResult.name)
                }
            }
        }
    }


    fun checkSlovo(word: String, callback: (Boolean) -> (Unit)) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val games = db.slovoDao().checkSlovo(word)
                callback(games.isNotEmpty())
            }
        }
    }

}