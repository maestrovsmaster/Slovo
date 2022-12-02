package com.maestrovs.slovo.screens.game_end

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.maestrovs.slovo.R
import com.maestrovs.slovo.databinding.FragmentGameEndBinding
import com.maestrovs.slovo.screens.base.BaseFragment
import com.maestrovs.slovo.screens.extensions.applyFont
import com.maestrovs.slovo.screens.MainActivity
import com.maestrovs.slovo.screens.MainViewModel
import com.maestrovs.slovo.screens.main.MainScreenFragmentDirections


class GameEndFragment : BaseFragment() {

    private val mainScreenViewModel by lazy {
        ViewModelProvider(activity as MainActivity).get(MainViewModel::class.java).apply {
            lifecycle.addObserver(this)
        }
    }

    private val gameEndViewModel by lazy {
        ViewModelProvider(this).get(GameEndViewModel::class.java).apply {
            lifecycle.addObserver(this)
        }
    }
    private var _binding: FragmentGameEndBinding? = null
    private val binding get() = _binding!!
    lateinit var mainActivity: MainActivity

    private val safeArgs: GameEndFragmentArgs by navArgs()

    var canShowMeaningOfWord = true;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        gameEndViewModel.navController = findNavController()
        _binding = FragmentGameEndBinding.inflate(inflater, container, false)
        val root: View = binding.root
        applyFont(requireContext(), root, "fonts/Comfortaa-Regular.ttf")

       // binding.title.text = safeArgs.slovo

        canShowMeaningOfWord = safeArgs.isWin

        binding.tvTitle.setTextColor(ContextCompat.getColor(requireContext(),
            if(safeArgs.isWin) {
                R.color.green
            }else{
                R.color.red
            }
        ))

        binding.tvTitle.text = if(safeArgs.isWin) {
            "Вірно!"
        }else{
            "Не вгадали"
        }

        binding.tvSlovo.visibility = if(safeArgs.isWin) {
            View.VISIBLE
        }else{
            View.VISIBLE
        }


        binding.tvSlovo.text = if(safeArgs.isWin) {
            safeArgs.slovo
        }else{
            safeArgs.slovo //"✜ ✜ ✜ ✜ ✜"
        }

        binding.btShow.visibility = if(safeArgs.isWin) {
            View.GONE
        }else{
            View.GONE
        }

        binding.btAgain.visibility = if(safeArgs.isWin) {
            View.GONE
        }else{
            View.GONE
        }

        binding.btMeaning.visibility = if(safeArgs.isWin) {
            View.VISIBLE
        }else{
            //View.GONE
            View.VISIBLE
        }

        binding.ivResult.setImageDrawable(ContextCompat.getDrawable(requireContext(),
        if(safeArgs.isWin){
            R.drawable.ic_orn_green
        }else{
            R.drawable.ic_orn_red
        }
            ))

        binding.btMeaning.setOnClickListener {
            safeArgs.slovo.let {
                findNavController().navigate(GameEndFragmentDirections.actionGameEndFragmentToWebViewFragment(it))
            }
        }

        binding.btNext.setOnClickListener {
            findNavController().popBackStack()
        }


        /*mainScreenViewModel.getGamesCount {
            Log.d("Game", "Games count = $it")
            if (it == null || it == 0)
            {
                binding.dicText.visibility = View.VISIBLE
                mainScreenViewModel.readDictionary { dic ->
                    var totalCnt = dic.size
                    binding.dicProgress.max = totalCnt
                    mainScreenViewModel.writeDictionaryToDB(dic, { progress ->
                       // Log.d("Game", "Progress = $progress")
                        binding.dicProgress.progress = progress
                    },
                        {
                            launchGame(false)
                        })
                }
            } else {
                launchGame(true)
            }
        }*/

       /// findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToWebViewFragment("слово"))

        return root
    }

    private fun launchGame(withDelay: Boolean) {
   /*     Handler(Looper.getMainLooper()).postDelayed(
            {
                splashViewModel.onMainScreen()

            }, if (withDelay) {
                delay
            } else {
                0
            }
        )*/
    }


    override fun onDestroyView() {
        super.onDestroyView()
        // binding.clMessage.animate().cancel()
        _binding = null
    }


}