package com.maestrovs.slovo.screens.game_end

import android.app.Application
import com.maestrovs.slovo.R
import com.maestrovs.slovo.screens.base.BaseViewModel

class GameEndViewModel(application: Application) : BaseViewModel(application) {




    fun onBack() {
        navController.popBackStack(R.id.fragment_main_screen, false)
    }




}