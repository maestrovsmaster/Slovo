package com.maestrovs.slovo.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.maestrovs.slovo.R
import com.maestrovs.slovo.databinding.ActivityMainBinding
import com.maestrovs.slovo.screens.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity(){

    override val fragmentContainerId: Int = R.id.nav_host_fragment_activity_main



    private lateinit var binding: ActivityMainBinding


    private val mainScreenViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java).apply {
            lifecycle.addObserver(this)
        }
    }







    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)




        /*mainScreenViewModel.message.observe(this, {
            Log.d("MainActivity", "We take message $it")

            val mySnackbar = Snackbar.make(container, it, 1000).show()

        })*/

      /*  mainScreenViewModel.readGames {
            Log.d("Game","Read Games... ${it.size}")
            Log.d("Game","Read Games... $it")
        }*/

       /* mainScreenViewModel.readDictionary {
            Log.d("Game","Complete read... $it")
            mainScreenViewModel.writeDictionaryToDB(it){
                Log.d("Game","Complete write to db...")
            }
        }*/


    }


    fun exitApp() {
       /* MaterialDialog(this).show {
            title(R.string.confirm_app_exit)
            positiveButton {
                SharedManager.setAcceptAgreement(this@MainActivity, false)
                auth.signOut()
                Handler(Looper.getMainLooper()).postDelayed({
                    exitProcess(0)
                }, 600)
                //
            }
            negativeButton {

            }
        }*/
    }


    override fun onBackPressed() {

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main);
        val fragmentList = navHostFragment?.getChildFragmentManager()?.getFragments()

        var handled = false
        fragmentList?.let {
            for (f in fragmentList) {

            }
            if (!handled) {
                super.onBackPressed()
            }
        }
    }


    companion object {
        fun callingIntent(context: Context) = Intent(context, MainActivity::class.java)
    }



}