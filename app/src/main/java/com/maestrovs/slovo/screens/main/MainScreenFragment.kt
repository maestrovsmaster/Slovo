package com.maestrovs.slovo.screens.main

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.maestrovs.slovo.R
import com.maestrovs.slovo.components.KeyboardProtocol
import com.maestrovs.slovo.components.UITilesRow
import com.maestrovs.slovo.databinding.FragmentMainScreenBinding
import com.maestrovs.slovo.model.Key
import com.maestrovs.slovo.model.KeyUI
import com.maestrovs.slovo.screens.MainActivity
import com.maestrovs.slovo.screens.about.AboutAppBottomSheetDialogFragment
import com.maestrovs.slovo.screens.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreenFragment : BaseFragment(), Observer<List<Any>>, GameInterface {

    private val mainScreenViewModel: GameViewModel by lazy {
        ViewModelProvider(activity as MainActivity).get(GameViewModel::class.java).apply {
            lifecycle.addObserver(this)
        }
    }

    private val gameManager by lazy {
        GameManager(requireContext(), mainScreenViewModel, this)
    }


    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!
    lateinit var mainActivity: MainActivity


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainScreenViewModel.navController = findNavController()
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.gameSmile.visibility = View.GONE

        val myString = "#слово"
        val spanFlag = Spannable.SPAN_INCLUSIVE_INCLUSIVE
        val spannableString = SpannableString(myString)
        val foregroundSpan = ForegroundColorSpan(Color.RED)
        spannableString.setSpan(foregroundSpan, 0, 1, spanFlag)

        binding.title.text = spannableString

        binding.statistic.setOnClickListener {
           // mainScreenViewModel.readStatistic {
           //     Log.d("Game", "games: $it")
           // }
            //
        }

        binding.webViewBt.setOnClickListener {
            gameManager.slovo.let {
                findNavController().navigate(
                    MainScreenFragmentDirections.actionFragmentMainScreenToWebViewFragment(
                        it.slovo
                    )
                )
            }
        }

        binding.about.setOnClickListener {
            showAboutAppDialog()
            //  findNavController().navigate(MainScreenFragmentDirections.actionFragmentMainScreenToAboutFragment())
        }

        binding.nextBt.setOnClickListener {
            binding.gameSmile.visibility = View.GONE
            gameManager.nextGame()
        }

        binding.keyboard.delegate = object : KeyboardProtocol {
            override fun onKey(keyUI: KeyUI) {
                gameManager.setKeyUI(keyUI)
            }
        }


        binding.webViewBt.visibility = View.GONE
        binding.nextBt.visibility = View.GONE

        return root
    }


    private fun showAboutAppDialog() {

        val bottomSheetFragment = AboutAppBottomSheetDialogFragment()
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)

        gameManager.init()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity

        gameManager.init()
        gameManager.nextGame()
    }


    override fun onResume() {
        super.onResume()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onChanged(value: List<Any>) {
    }


    override fun initRows(rows: MutableList<UITilesRow>): MutableList<UITilesRow> {
        rows.clear()
        rows.add(binding.word1)
        rows.add(binding.word2)
        rows.add(binding.word3)
        rows.add(binding.word4)
        rows.add(binding.word5)
        rows.add(binding.word6)
        rows.add(binding.word7)
        return rows
    }

    override fun updateKeyboard(keyboardKeys: MutableList<Key>) {
        binding.keyboard.updateKeys(keyboardKeys)
    }

    override fun showWrongWordAnimation() {
        //Nothing
        Toast.makeText(context, "Такого слова в грі немає", Toast.LENGTH_SHORT).show()
    }

    override fun resetGame() {
        binding.gameSmile.visibility = View.GONE
        binding.webViewBt.visibility = View.GONE
        binding.nextBt.visibility = View.GONE
        binding.keyboard.resetKeys()
    }

    val ngDelay = 1000L

    override fun showWin() {
       // mainScreenViewModel.resetSavedGame()
        Handler(Looper.getMainLooper()).postDelayed({

            findNavController().navigate(
                MainScreenFragmentDirections.actionFragmentMainScreenToGameEndFragment(
                    gameManager.slovo.slovo,
                    true
                )
            )

        }, ngDelay)
    }

    override fun showFail() {
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(
                MainScreenFragmentDirections.actionFragmentMainScreenToGameEndFragment(
                    gameManager.slovo.slovo,
                    false
                )
            )
        }, ngDelay)
    }

    private fun smileWin() {
        binding.nextBt.visibility = View.VISIBLE
        binding.webViewBt.visibility = View.VISIBLE
        binding.gameSmile.visibility = View.VISIBLE
        binding.gameSmile.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.smile_glad_green
            )
        )
    }

    private fun smileLost() {
        binding.nextBt.visibility = View.VISIBLE
        binding.webViewBt.visibility = View.VISIBLE
        binding.gameSmile.visibility = View.VISIBLE
        binding.gameSmile.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.smile_sad
            )
        )
    }

    override fun showNotComplete() {
        //Nothing
    }




}