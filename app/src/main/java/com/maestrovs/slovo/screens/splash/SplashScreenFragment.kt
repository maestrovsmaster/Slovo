package com.maestrovs.slovo.screens.splash

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.maestrovs.slovo.databinding.FragmentSplashBinding
import com.maestrovs.slovo.screens.base.BaseFragment
import com.maestrovs.slovo.screens.extensions.applyFont
import com.maestrovs.slovo.screens.MainActivity
import com.maestrovs.slovo.screens.MainViewModel


class SplashScreenFragment : BaseFragment() {

    private val mainScreenViewModel by lazy {
        ViewModelProvider(activity as MainActivity).get(MainViewModel::class.java).apply {
            lifecycle.addObserver(this)
        }
    }

    private val splashViewModel by lazy {
        ViewModelProvider(this).get(SplashScreenViewModel::class.java).apply {
            lifecycle.addObserver(this)
        }
    }
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    lateinit var mainActivity: MainActivity

    private val delay = 30L

    fun getConnectivityManager() =
        requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        splashViewModel.navController = findNavController()
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        val root: View = binding.root
        applyFont(requireContext(), root, "fonts/Comfortaa-Regular.ttf")


        mainScreenViewModel.getGamesCount {
            Log.d("Game", "Games count = $it")
            if (it == null || it == 0) {
                binding.dicText.visibility = View.VISIBLE
                mainScreenViewModel.readDictionary { dic ->
                    var totalCnt = dic.size
                    binding.dicProgress.max = totalCnt
                    mainScreenViewModel.writeDictionaryToDB(dic,
                        {
                            launchGame(false)
                        })
                }
            } else {
                launchGame(true)
            }
        }

        /// findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToWebViewFragment("слово"))

        return root
    }

    private fun launchGame(withDelay: Boolean) {
        Handler(Looper.getMainLooper()).postDelayed(
            {
                splashViewModel.onMainScreen()

            }, if (withDelay) {
                delay
            } else {
                0
            }
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        // binding.clMessage.animate().cancel()
        _binding = null
    }


}