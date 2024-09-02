package com.maestrovs.slovo.screens.main

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.maestrovs.slovo.common.SharedManager
import com.maestrovs.slovo.data.dao.AppDatabase
import com.maestrovs.slovo.data.dao.Slovo
import com.maestrovs.slovo.screens.base.BaseViewModel
import com.maestrovs.slovo.screens.extensions.addTo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(application: Application, private val db: AppDatabase) : BaseViewModel(application) {


    var slovo: String? = null

    var attempt = 0
    var attemptSlovo: String? = null;


    //private var savedSlovo: String? = null
    private var savedWords: HashSet<String> = hashSetOf()

    private var restoredSlovo: String? = null

    fun restoreSlovo(){
        restoredSlovo = readLastSlovo()
        // savedWords = readLastWords()
    }

    fun saveCurrentSlovo(slovo:String){
        SharedManager.writeLastSlovo(slovo,context)
    }

    fun readLastSlovo() = SharedManager.readLastSlovo(context)

    fun saveAddUserWord(userWord: String){
        savedWords.add(userWord)
        SharedManager.writeWordsList(savedWords, context)
    }

    fun readLastWords() = SharedManager.readWordsList(context)

    fun resetSavedGame(){
        SharedManager.writeLastSlovo(null,context)
        SharedManager.writeWordsList(null,context)
    }



    fun selectRandomSlovo(callback: (Slovo, savedSteps:List<String>?)->(Unit)){
        var list : MutableList<String>? = null
        val savedSteps = readLastWords()
        if(savedSteps.isNullOrEmpty()){
            list = null
        }else{
            list = mutableListOf()
            savedSteps.map {
                list.add(it)
            }
        }

        if(restoredSlovo != null && !savedSteps.isNullOrEmpty()){
            slovo = restoredSlovo!!
            attemptSlovo = null; attempt = 0;
            callback(Slovo(restoredSlovo!!, null,0,0,false), list)
        }else if(attemptSlovo != null) {
            slovo = attemptSlovo!!
            callback(Slovo(attemptSlovo!!, null,0,0,false), list)
        }else{
            attemptSlovo = null; attempt = 0;
            BehaviorSubject.create { emitter: ObservableEmitter<List<Slovo>> ->
                val games = db.slovoDao().getRandomSlovo()
                emitter.onNext(games)
            }.hide()
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe({
                    if (it.isEmpty()) {
                        callback(Slovo("ФІНІШ", null,0,0,false),null)
                    } else {
                        slovo = it[0].slovo
                        callback(it[0], null)
                    }

                }, {
                    errorMessage.postValue(it.localizedMessage)
                }).addTo(disposable)
        }

    }

    fun updateSlovoStepInDB(slovo: Slovo){
        Completable.create { emitter ->
            db.slovoDao().updateSlovoStep(slovo.slovo?:"",slovo.step?:0)
            emitter.onComplete()}
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe({
                Log.d("ServiceResponse","msgsucc")
                // callback()
            }, {
                Log.d("ServiceResponse","err = $it")
                errorMessage.postValue(it.localizedMessage)

            }).addTo(disposable)
    }

    fun writeGameToDB(slovo: Slovo){
        Completable.create { emitter ->
            db.slovoDao().insertAll(slovo)
            emitter.onComplete()}
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe({
                Log.d("ServiceResponse","msgsucc")
                // callback()
            }, {
                Log.d("ServiceResponse","err = $it")
                errorMessage.postValue(it.localizedMessage)

            }).addTo(disposable)
    }

    fun readStatistic(callback: (List<Slovo>)->(Unit)){
        BehaviorSubject.create { emitter: ObservableEmitter<List<Slovo>> ->
            val games = db.slovoDao().getPlayedGames()
            emitter.onNext(games)
        }.hide()
            .observeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe({
                callback(it)
            }, {
                errorMessage.postValue(it.localizedMessage)
            }).addTo(disposable)
    }

    fun checkSlovo(word: String, callback: (Boolean)->(Unit)){
        BehaviorSubject.create { emitter: ObservableEmitter<List<Slovo>> ->
            val games = db.slovoDao().checkSlovo(word)
            emitter.onNext(games)
        }.hide()
            .observeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe({
                callback(it.isNotEmpty())
            }, {
                errorMessage.postValue(it.localizedMessage)
            }).addTo(disposable)
    }


    fun readGames(callback: (List<Slovo>)->(Unit)){
        BehaviorSubject.create { emitter: ObservableEmitter<List<Slovo>> ->
            val games = db.slovoDao().getAll()
            emitter.onNext(games)
        }.hide()
            .observeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe({
                callback(it)
            }, {
                errorMessage.postValue(it.localizedMessage)
            }).addTo(disposable)
    }


}