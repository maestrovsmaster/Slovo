package com.maestrovs.slovo.screens

import android.app.Application
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.maestrovs.slovo.common.SharedManager
import com.maestrovs.slovo.data.dao.AppDatabase
import com.maestrovs.slovo.data.dao.Slovo
import com.maestrovs.slovo.screens.base.BaseViewModel
import com.maestrovs.slovo.screens.extensions.addTo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(application: Application, private val db: AppDatabase) : BaseViewModel(application) {


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

    fun writeDictionaryToDB(
        dictionary: List<String>,
        progress: (Int) -> Unit,
        onComplete: () -> Unit
    ) {
        val disposable = CompositeDisposable()

        val slovos = dictionary.map {

            val result = parseSlovo(it)
            val slovo = result.first.uppercase()
            val level = result.second

            Slovo(slovo,level, 0, 0, false) }.toTypedArray()

        Observable.fromCallable {
            db.slovoDao().insertAll(*slovos)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                progress(dictionary.size)
                onComplete()
            }, {
                // Обробка помилок
            }).addTo(disposable)
    }

    fun parseSlovo(input: String): Pair<String, Int?> {
        val regex = Regex("(.+)-(.+)")
        val matchResult = regex.matchEntire(input)

        return if (matchResult != null) {
            val (word, number) = matchResult.destructured
            val numberInt = number.toIntOrNull()
            Pair(word, numberInt)
        } else {
            Pair(input, null)
        }
    }



    private fun readDicFromFile(): Observable<List<String>>{
        val observable =
            BehaviorSubject.create { emitter: ObservableEmitter<List<String>> ->
                val list = mutableListOf<String>()
                val inputStream: InputStream = context.getResources().getAssets().open("dictionary_new")
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

    fun getGamesCount(callback: (Int?)->(Unit)){
        BehaviorSubject.create { emitter: ObservableEmitter<Int> ->
            val games = db.slovoDao().getGamesCount()
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