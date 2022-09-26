package com.maestrovs.slovo.screens.splash

import android.app.Application
import com.maestrovs.slovo.screens.base.BaseViewModel

class SplashScreenViewModel(application: Application) : BaseViewModel(application) {




    fun onBack() {
      //  navController.popBackStack(R.id.fragment_main_screen, false)
    }



    fun onMainScreen() {
        navController.navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToFragmentMainScreen())


       /// navController.navigate(R.id.schedulersFragment)

     ///   navController.navigate(R.id.feedbackFragment)
    }


   /* fun onPhoneVerification() {
        navController.navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToPhoneVerificationFragment())

    }*/





    init {
    }


}