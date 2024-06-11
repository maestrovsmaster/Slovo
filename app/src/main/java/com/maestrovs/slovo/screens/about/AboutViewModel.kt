package com.maestrovs.slovo.screens.about

import android.app.Application
import com.maestrovs.slovo.R
import com.maestrovs.slovo.screens.base.BaseViewModel

class AboutViewModel(application: Application) : BaseViewModel(application) {




    fun onBack() {
        navController.popBackStack(R.id.fragment_main_screen, false)
    }





}