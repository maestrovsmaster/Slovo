package com.maestrovs.slovo.screens

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


@SuppressLint("CustomSplashScreen")
class StartActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        startActivity(MainActivity.callingIntent(this))
        finish()


    }




}