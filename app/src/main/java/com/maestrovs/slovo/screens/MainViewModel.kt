package com.maestrovs.slovo.screens

import android.app.Application
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.maestrovs.slovo.common.SharedManager
import com.maestrovs.slovo.data.AppDatabase
import com.maestrovs.slovo.data.Game
import com.maestrovs.slovo.screens.base.BaseViewModel
import com.maestrovs.slovo.screens.extensions.addTo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


class MainViewModel(application: Application) : BaseViewModel(application) {

    internal val message = MutableLiveData<String>()

    var db = Room.databaseBuilder(
        application.applicationContext,
    AppDatabase::class.java, "database-name"
    ).build()

    var context: Context = application.applicationContext


    var slovo: String? = null


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


    fun getGamesCount(callback: (Int?)->(Unit)){
        BehaviorSubject.create { emitter: ObservableEmitter<Int> ->
            val games = db.userDao().getGamesCount()
            emitter.onNext(games)
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe({
                callback(it)
            }, {
                errorMessage.postValue(it.localizedMessage)
                callback(null)
            }).addTo(disposable)
    }



    fun selectRandomSlovo(callback: (Game, savedSteps:List<String>?)->(Unit)){
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
            callback(Game(restoredSlovo!!, 0), list)
        }else {

            BehaviorSubject.create { emitter: ObservableEmitter<List<Game>> ->
                val games = db.userDao().getRandomSlovo()
                emitter.onNext(games)
            }.hide()
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe({
                    if (it.isEmpty()) {
                        callback(Game("ФІНІШ", 0),null)
                    } else {
                        slovo = it[0].slovo
                        callback(it[0], null)
                    }

                }, {
                    errorMessage.postValue(it.localizedMessage)
                }).addTo(disposable)
        }
    }

    fun updateSlovoStepInDB(game: Game){
        Completable.create { emitter ->
            db.userDao().updateSlovoStep(game.slovo?:"",game.step?:0)
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

    fun writeGameToDB(game: Game){
        Completable.create { emitter ->
            db.userDao().insertAll(game)
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

    fun readStatistic(callback: (List<Game>)->(Unit)){
        BehaviorSubject.create { emitter: ObservableEmitter<List<Game>> ->
            val games = db.userDao().getPlayedGames()
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
        BehaviorSubject.create { emitter: ObservableEmitter<List<Game>> ->
            val games = db.userDao().checkSlovo(word)
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


    fun readGames(callback: (List<Game>)->(Unit)){
        BehaviorSubject.create { emitter: ObservableEmitter<List<Game>> ->
            val games = db.userDao().getAll()
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


    fun readDictionary(callback: (List<String>)->(Unit)){
        readDicFromFile()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe({
                callback(it)
            }, {
                errorMessage.postValue(it.localizedMessage)
                Log.d("Game","Dictвівв: $it")
            }).addTo(disposable)
    }

    fun writeDictionaryToDB(dictionary: List<String>,progress:  (Int)->(Unit),callback: ()->(Unit)){
        var count = 0
        dictionary.map {
            Completable.create { emitter ->
                db.userDao().insertAll(Game(it.uppercase(),0))
                emitter.onComplete()}
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe({
                    Log.d("ServiceResponse","msgsucc")
                    count +=1
                    progress(count)
                    if(count == dictionary.size) callback()
                }, {
                    count +=1
                    progress(count)
                    Log.d("ServiceResponse","err = $it")
                    errorMessage.postValue(it.localizedMessage)
                    if(count == dictionary.size) callback()

                }).addTo(disposable)
        }
    }




    private fun readDicFromFile(): Observable<List<String>>{
        val observable =
            BehaviorSubject.create { emitter: ObservableEmitter<List<String>> ->
                val list = mutableListOf<String>()
                val inputStream: InputStream = context.getResources().getAssets().open("dictionary");//getResources().openRawResource(R.raw.dictionary)
                val bufferedReader = BufferedReader(InputStreamReader(inputStream))

                while (bufferedReader.ready()) {
                    val line: String = bufferedReader.readLine()
                   // Log.d("Game","gamesвввв: $line")
                    list.add(line)
                }

                emitter.onNext(list)
                //emitter.setCancellable { listeningRegistration.remove() }
            }
        return observable.hide()
            .observeOn(Schedulers.io())
    }




    fun showMessage(message: String){
        this.message.postValue(message)
    }

    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location>
        get() = _location

    fun setLocation(location: Location) {
        _location.value = location

    }


    lateinit var app: Application

    init {
        this.app = application
    }


}