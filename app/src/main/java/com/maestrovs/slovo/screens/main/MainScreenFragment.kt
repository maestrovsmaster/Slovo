package com.maestrovs.slovo.screens.main

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.maestrovs.slovo.R
import com.maestrovs.slovo.components.KeyboardProtocol
import com.maestrovs.slovo.components.OpenStatus
import com.maestrovs.slovo.data.Game
import com.maestrovs.slovo.databinding.FragmentMainScreenBinding
import com.maestrovs.slovo.model.Key
import com.maestrovs.slovo.model.KeyType
import com.maestrovs.slovo.model.KeyUI
import com.maestrovs.slovo.model.isMoreWeight
import com.maestrovs.slovo.screens.MainActivity
import com.maestrovs.slovo.screens.MainViewModel
import com.maestrovs.slovo.screens.about.AboutAppBottomSheetDialogFragment
import com.maestrovs.slovo.screens.base.BaseFragment
import com.maestrovs.slovo.screens.extensions.applyFont


class MainScreenFragment : BaseFragment(), Observer<List<Any>> {

    // var db: AppDatabase? = null


    private val mainScreenViewModel by lazy {
        ViewModelProvider(activity as MainActivity).get(MainViewModel::class.java).apply {
            lifecycle.addObserver(this)
        }
    }

    var rows: MutableList<com.maestrovs.slovo.components.UITilesRow> = mutableListOf()

    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!
    lateinit var mainActivity: MainActivity


    var gameNum = 0


    var slovo = "ПОШТА"
    var step = 0
    var lastStep = 5
    val keyboardKeys: MutableList<Key> = mutableListOf()
    var currentRow: com.maestrovs.slovo.components.UITilesRow? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mainScreenViewModel.navController = findNavController()
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        val root: View = binding.root
        // applyFont(requireContext(), root, "fonts/Comfortaa-Regular.ttf")

        //  binding.btMenuShipments onClick mainScreenViewModel.onShipments()
        binding.gameSmile.visibility = View.GONE

        val myString = "#слово"
        val spanFlag = Spannable.SPAN_INCLUSIVE_INCLUSIVE
        val spannableString = SpannableString(myString)
        val foregroundSpan = ForegroundColorSpan(Color.RED)
        spannableString.setSpan(foregroundSpan, 0, 1, spanFlag)

        binding.title.text = spannableString

        binding.statistic.setOnClickListener {
            // val games = db!!.userDao().getAll()
            Log.d("Game", "games..")
            mainScreenViewModel.readStatistic {
                Log.d("Game", "games: $it")
            }
            //
        }

        binding.webViewBt.setOnClickListener {
            slovo.let {
                findNavController().navigate(
                    MainScreenFragmentDirections.actionFragmentMainScreenToWebViewFragment(
                        it
                    )
                )
            }
        }

        binding.about.setOnClickListener {

            showAboutAppDialog()

            //  findNavController().navigate(MainScreenFragmentDirections.actionFragmentMainScreenToAboutFragment())
        }

        binding.nextBt.setOnClickListener {
            nextGame()
        }
        binding.webViewBt.visibility = View.GONE
        binding.nextBt.visibility = View.GONE

        return root
    }


    private fun showAboutAppDialog() {

        val bottomSheetFragment = AboutAppBottomSheetDialogFragment()
        bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)

        /*  val dialog = Dialog(requireContext(), R.style.DialogStyle)
          dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
          dialog.setCancelable(true)
          dialog.setContentView(R.layout.fragment_about)
          val window = dialog.window!!
          window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
          window.setGravity(Gravity.BOTTOM)
          window.decorView.setBackgroundResource(android.R.color.transparent)
          dialog.show()*/
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity

        // Log.d("Game","onViewCreatedSavedSlovo = ${mainScreenViewModel.readLastSlovo()}")
        //Log.d("Game","onViewCreatedSavedWords = ${mainScreenViewModel.readLastWords()}")
        mainScreenViewModel.restoreSlovo()

        keyboardKeys.clear()

        initRows()

        nextGame()

        binding.keyboard.delegate = object : KeyboardProtocol {
            override fun onKey(keyUI: KeyUI) {

                when (keyUI.type) {
                    KeyType.Letter -> currentRow?.addKey(keyUI.key)
                    KeyType.Enter -> {
                        val usersEntered = currentRow?.getUserWord() ?: ""
                        Log.d("Game", "Check users word: $usersEntered")
                        mainScreenViewModel.checkSlovo(usersEntered) { correct ->
                            Log.d("Game", "Check users correct: $correct")
                            if (correct) {
                                val status = currentRow?.openSlovo()
                                Log.d("Game", "Statuss=$status")
                                processGameStatus(status!!)
                            } else {
                                showWrongWordAnim()
                            }
                        }

                    }

                    KeyType.Backspace -> {
                        currentRow?.removeLast()
                    }
                }
            }
        }
    }


    fun processGameStatus(status: OpenStatus) {

        when (status) {
            OpenStatus.Win -> {
                mainScreenViewModel.updateSlovoStepInDB(Game(slovo, step + 1, 0, false))
                showWin()
            }

            OpenStatus.Game -> {

                val lastWord = currentRow?.getUserWord()
                lastWord?.let {
                    mainScreenViewModel.saveAddUserWord(it)
                }

                Log.d(
                    "Game",
                    "lastWord = $lastWord   slovo=$slovo step =$step lastStep = $lastStep"
                )

                val nextKeys = currentRow?.getKeyboardActualKeys()
                if (canNext()) {
                    nextStep()
                    addKeysToExists(nextKeys ?: mutableListOf())
                    binding.keyboard.updateKeys(keyboardKeys)
                } else {
                    //Fail
                    if (step >= 0) {
                        step = -1 //Можна повторну спробу вгадати слово
                    } else {
                        step = -2 //Немає більше спроб вгадати слово
                    }
                    mainScreenViewModel.updateSlovoStepInDB(Game(slovo, step, 0, false))
                    showFail()
                }
            }

            OpenStatus.WrongWord -> {
                //покажем анімашку, що хибне слово
                showWrongWordAnim()
            }

            OpenStatus.NoComplete -> {
                // покажем анімашку пусті клітини
                currentRow?.showNotCompleteTiles()
            }
        }
    }


    fun initRows() {
        rows.clear()
        rows.add(binding.word1)
        rows.add(binding.word2)
        rows.add(binding.word3)
        rows.add(binding.word4)
        rows.add(binding.word5)
        rows.add(binding.word6)
        rows.add(binding.word7)
    }


    fun addKeysToExists(adds: List<Key>) {


        adds.map { letter ->
            Log.d("KeysKL", "letter = $letter")

            var curentLetter = letter

            val sameLs = adds.filter { ds -> ds.value == letter.value }
            Log.d("KeysKL", "sameLs = $sameLs")
            sameLs.map { same ->
                if (same.state.isMoreWeight(curentLetter.state)) {
                    curentLetter = same
                }
            }

            if (!keyboardKeys.contains(curentLetter)) {
                Log.d("KeysKL", "add + $curentLetter")
                keyboardKeys.add(curentLetter)
            } else {
                var curentLetter2 = curentLetter
                val sameLs = keyboardKeys.filter { ds -> ds.value == curentLetter.value }
                if (sameLs.isNotEmpty()) {
                    if (sameLs[0].state.isMoreWeight(curentLetter2.state)) {
                        curentLetter2 = sameLs[0]
                    } else {
                    }
                } else {
                    //Nothing
                }
                keyboardKeys.add(curentLetter2)
            }
        }
    }

    fun nextGame() {

        binding.gameSmile.visibility = View.GONE

        mainScreenViewModel.selectRandomSlovo { sl, savedSteps ->
            slovo = sl.slovo ?: "СЛОВО"
            Log.d("Game", "RandomSlovo = $sl")
            Log.d("Game", "SavedSteps = $savedSteps")
            //

            resetGame()
            currentRow!!.slovo = slovo
            mainScreenViewModel.saveCurrentSlovo(slovo)
            gameNum += 1
            if (!savedSteps.isNullOrEmpty()) {
                restoreGame(savedSteps)

            }
        }
    }

    fun restoreGame(words: List<String>) {
        resetGame()
        currentRow!!.slovo = slovo
        words.map {
            val status = currentRow!!.forceOpenSlovo(it)
            processGameStatus(status)
        }
    }


    private fun nextStep() {
        step += 1
        currentRow = rows[step]!!
        currentRow!!.slovo = slovo

    }

    private fun canNext(): Boolean {
        return step < lastStep
    }

    fun resetGame() {
        step = 0
        binding.gameSmile.visibility = View.GONE
        binding.webViewBt.visibility = View.GONE
        binding.nextBt.visibility = View.GONE
        mainScreenViewModel.resetSavedGame()
        keyboardKeys.clear()
        binding.keyboard.resetKeys()
        rows.map {
            it.slovo = ""
            it.clearSlovo()
        }
        currentRow = rows[0]
    }

    val ngDelay = 1000L

    fun showWin() {

        mainScreenViewModel.resetSavedGame()
        Handler(Looper.getMainLooper()).postDelayed({
            /*binding.nextBt.visibility = View.VISIBLE
            binding.webViewBt.visibility = View.VISIBLE
            binding.gameSmile.visibility = View.VISIBLE
            binding.gameSmile.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.smile_glad_green));*/
            findNavController().navigate(
                MainScreenFragmentDirections.actionFragmentMainScreenToGameEndFragment(
                    slovo,
                    true
                )
            )

        }, ngDelay)

    }

    fun showFail() {

        mainScreenViewModel.resetSavedGame()
        Handler(Looper.getMainLooper()).postDelayed({
            /*  binding.nextBt.visibility = View.VISIBLE
              binding.webViewBt.visibility = View.VISIBLE
              binding.gameSmile.visibility = View.VISIBLE
              binding.gameSmile.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.smile_sad));*/
            findNavController().navigate(
                MainScreenFragmentDirections.actionFragmentMainScreenToGameEndFragment(
                    slovo,
                    false
                )
            )
        }, ngDelay)
    }

    fun showWrongWordAnim() {
        currentRow?.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.shake))
        // val v = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        //v!!.vibrate(300)
        Toast.makeText(requireContext(), "Такого слова в грі немає", Toast.LENGTH_SHORT).show()
    }


    override fun onResume() {
        super.onResume()

        Log.d("Game", "SavedSlovo = ${mainScreenViewModel.readLastSlovo()}")
        Log.d("Game", "SavedWords = ${mainScreenViewModel.readLastWords()}")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        // binding.clMessage.animate().cancel()

        // mainScreenViewModel.savedSlovo = slovo
        _binding = null
    }


    override fun onChanged(t: List<Any>?) {
    }


    /* onBackPressed(): Boolean {
        showCancelReservationActionForBack()
        return true
    }*/


}