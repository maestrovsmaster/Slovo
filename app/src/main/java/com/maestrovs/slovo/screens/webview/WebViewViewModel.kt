package com.maestrovs.slovo.screens.webview

import android.app.Application
import android.util.Log
import com.maestrovs.slovo.data.repository.MainRepository
import com.maestrovs.slovo.screens.base.BaseViewModel
import com.maestrovs.slovo.screens.extensions.addTo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import okhttp3.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import javax.inject.Inject


@HiltViewModel
class WebViewViewModel @Inject constructor(
    val repository: MainRepository,
    application: Application
) : BaseViewModel(application) {


    fun getSlovo(slovo: String, callback: (String) -> (Unit)) {
        var result: String = ""
        BehaviorSubject.create { emitter: ObservableEmitter<String> ->
            val doc = Jsoup.connect("http://sum.in.ua/?swrd=$slovo").get()
            val article: Element? = doc.getElementById("article")
            emitter.onNext(article?.text() ?: "")
        }.hide()
            .observeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.newThread())
            .subscribe({
                callback(it)
            }, {
                Log.d("Game", "errrrr = ${it.localizedMessage}")
                errorMessage.postValue(it.localizedMessage)
            }).addTo(disposable)

    }


}