package com.maestrovs.slovo.screens.base

import android.app.Application
import android.content.Context
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import io.reactivex.rxjava3.disposables.CompositeDisposable

//import io.reactivex.disposables.CompositeDisposable

const val KEY_TOKEN_EXPIRED = "401"


open class BaseViewModel(application: Application) : AndroidViewModel(application),
    LifecycleObserver {

    internal val message = MutableLiveData<String>()

    var context: Context = application.applicationContext


    protected val disposable = CompositeDisposable()


    lateinit var navController: NavController

    val progressState: LiveData<Boolean> get() = isProgress
    var isProgress = MutableLiveData<Boolean>()





    val errorState: LiveData<String> get() = isError
    var isError = MutableLiveData<String>()

    val successState: LiveData<Boolean> get() = isSuccess
    var isSuccess = MutableLiveData<Boolean>()

    val tokenState: LiveData<String> get() = isGetToken
    var isGetToken = MutableLiveData<String>()

    val errorMessage = MutableLiveData<String>()



    init {
        isProgress.value = false
        isError.value = ""
        isSuccess.value = false
        isGetToken.value = ""
      //  (application as ALRDriver).appComponent.inject(this)
    }

    fun onError(error: String) {
//        if (error.equals(KEY_TOKEN_EXPIRED)) {
//            navController.graph.startDestination = R.id.fragment_login
//            navController.popBackStack(R.id.mobile_navigation, true)
//            navController.navigate(R.id.fragment_login)
//        } else {
//            isProgress.value = false
//            isError.value = error
//        }
    }

    fun onSuccess() {
        isSuccess.value = true
    }

    fun onGetToken(token: String) {
        isGetToken.postValue(token)
    }




}