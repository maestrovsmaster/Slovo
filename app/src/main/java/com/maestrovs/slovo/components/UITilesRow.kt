package com.maestrovs.slovo.components

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.maestrovs.slovo.R
import com.maestrovs.slovo.common.WordInspector
import com.maestrovs.slovo.model.*

class UITilesRow : FrameLayout {


    protected var mRootView: View? = null

    var tiles: MutableList<UITile> = mutableListOf()

    protected var tile0: UITile? = null
    protected var tile1: UITile? = null
    protected var tile2: UITile? = null
    protected var tile3: UITile? = null
    protected var tile4: UITile? = null


    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(context)
    }

    open val layoutID: Int = R.layout.component_tiles_row

    protected open fun init(context: Context) {

        foregroundGravity = Gravity.CENTER_VERTICAL

        val inflater = LayoutInflater.from(context)
        mRootView =
            inflater.inflate(layoutID, this)
        prepareItems()

        //DUMMY DATA
    }

    var slovo: String = ""

    private var keys: MutableList<Key> = mutableListOf()

    fun addKey(key: Key) {

        val mKey = Key(key.value, key.state)

        if (keys.size < 5) {
            this.keys.add(keys.size, mKey)//[keys.size] = key
            tiles[keys.size - 1].setKey(mKey)
        }
    }

    fun removeLast() {

        if (keys.size > 0) {
            this.keys.removeAt(keys.size - 1)//remove(keys.size - 1)
            tiles[keys.size].setKey(null)
        }


    }

    private val animStep = 100



    /**
     * Return actual keys for mark
     */
    fun openSlovo(): OpenStatus {
        if (keys.size == 5) {
            if(WordInspector.isWordWrong(getUserWord())){
                return OpenStatus.WrongWord
            }else {

                refreshKeysBySlovo()
                refreshTilesKeys()

                for (i in 0..4) {

                    val delay: Long = (i * animStep).toLong()

                    Handler(Looper.getMainLooper()).postDelayed({
                        tiles[i].flip()
                    }, delay)
                }
            }

            if(isWin()){
                return OpenStatus.Win
            }else
                return  OpenStatus.Game
        } else {
            return OpenStatus.NoComplete
        }
    }


    fun forceOpenSlovo(word: String): OpenStatus {
        word.map { c ->
            KeysList.keyByString(c.toString())?.let { keyUI ->
                addKey(keyUI.key)
            }
        }

        if (keys.size == 5) {
            if(WordInspector.isWordWrong(getUserWord())){
                return OpenStatus.WrongWord
            }else {

                refreshKeysBySlovo()
                refreshTilesKeys()

                for (i in 0..4) {

                    val delay: Long = (i * animStep).toLong()

                    //Handler(Looper.getMainLooper()).postDelayed({
                        tiles[i].flip()
                   // }, delay)
                }
            }

            if(isWin()){
                return OpenStatus.Win
            }else
                return  OpenStatus.Game
        } else {
            return OpenStatus.NoComplete
        }
    }


    fun getKeyboardActualKeys() = keys



    private fun refreshKeysBySlovo() {
        if (keys.size < 5) return
        for (i in 0..4) {

            var newKeyStatus = KeyState.Wrong

            val userLetter = keys[i]!!.value
            val slovoLetter = slovo.get(i).toString()

            if (slovo.contains(userLetter)) {
                newKeyStatus = KeyState.Mistake
            }


            if (userLetter == slovoLetter) {
                newKeyStatus = KeyState.Right
            }

            keys.get(i).state = newKeyStatus

        }
        clearExtraLetters()
    }

    //After refreshKeysBySlovo()
    fun clearExtraLetters() {
        if (keys.size < 5) return

        var userString = ""
        keys.map {
            userString += it.value
        }

        //Запам"ятаємо обрацьовані вже літери
        var processedLetters: MutableList<Char> = mutableListOf()

        for (i in 0..4) {

            //val slovoLetter = slovo.get(i)
            val userLetter = keys[i].value
            val userChar = userLetter.toCharArray()[0]

            if (processedLetters.contains(userChar)) continue

            if (slovo.contains(userLetter)) {
                //Check next...
                //скільки разів зустрічається ця буква в СЛОВІ
                val countOfSameSlovoSymbols = slovo
                    .filter { it == userChar }
                    .length

                //скільки разів цю букву ввів юзер
                val countOfSameUserSymbols = userString
                    .filter { it == userChar }
                    .length
                if (countOfSameUserSymbols > countOfSameSlovoSymbols) {

                    processedLetters.add(userChar)

                    //кількість однакових букв введених юзером перевищила кількість наявних в слові

                    //АкулА  :    оААвА ->  оАавА     /// aaaaa -> AaaaA

                    //Запам"ятаємо індекси  букв, що збігаються
                    var rightIndexes: MutableList<Int> = mutableListOf()

                    //спочатку шукаємо і лишаємо ті букви, що на свому місці
                    for (j in 0..4) {
                        val userLet = keys[j]!!.value
                        val slovoLet = slovo.get(j).toString()
                        if (userLet == slovoLet  && userLet == userChar.toString()) { //Test it
                            rightIndexes.add(j)
                        }
                    }
                    //вираховуємо решту букв не на своєму місці:
                    val restOutOfSlovo = countOfSameSlovoSymbols - rightIndexes.size
                    val restOutOfUser = countOfSameUserSymbols - rightIndexes.size

                    //тепер рахуємо скільки однакових букв залишаємо з юзерського вводу
                    val dif = restOutOfUser - restOutOfSlovo

                    var countLeaves = 0
                    //Запам"ятаємо індекси співпадаючих букв
                    val leavesIndexes: MutableList<Int> = mutableListOf()

                    //Далі проходимося по списку юзерських букв, оминаючи правильні індекси, які ми запам"ятали,
                    //лишаємо неспівпадаючі букви, поки їх кількість countLeaves не стане рівною dif
                    //індекси залишених неспівпадаючих букв також додаємо в захищені індекси leavesIndexes

                    //Шукаємо індекси не на свому місці за умови, що restOutOfSlovo>0

                    if (restOutOfSlovo > 0) {
                        for (j in 0..4) {
                            if (rightIndexes.contains(j)) continue
                            val userLet = keys[j]!!.value
                            if (slovo.contains(userLet)) {
                                leavesIndexes.add(j)
                                countLeaves += 1
                                if (countLeaves == dif) {
                                    break
                                }
                            }
                        }
                    }

                  //  val a = true

                    //тепер ми знаємо індекси співпадаючих букв rightIndexes і знаємо індекси букв неспівпадаючих,
                    // але які ми по кількості пропускаємо leavesIndexes

                    //тепер знову проходимо по списку, пропускаючи всі вищезгадані індекси
                    //і решту міняємо статус на None
                    for (j in 0..4) {
                        if (rightIndexes.contains(j)) continue
                        if (leavesIndexes.contains(j)) continue
                        val userLet = keys[j]!!.value
                        if(userLet != userLetter) continue //пропускаємо інші букви
                        if (slovo.contains(userLet)) {
                            keys[j]!!.state = KeyState.Wrong
                        }
                    }

                    val b = true
                }
            } else {
                //skip
            }
        }
    }


    private fun refreshTilesKeys() {
        if (keys.size < 5) return
        for (i in 0..4) {
            tiles[i].setKey(keys!![i])
        }
    }

    private fun getListForKeyboard(): List<Key> {
        val list: MutableList<Key> = mutableListOf()
        for (i in 0 until keys.size) {
            val item = keys[i]!!
            if (!list.contains(item)) {
                list.add(item)
            }
        }
        return list
    }

     fun getUserWord(): String{
        var userSlovo = ""
        for (i in 0 until keys.size) {
            val item = keys[i]!!
            userSlovo += item.value
        }
        return userSlovo
    }

    private  fun isWin(): Boolean {
        if (keys.size < 5) return false
        else {

            return getUserWord() == slovo
        }
    }

    fun showNotCompleteTiles(){
        tiles.map {
            if(it.getKey() == null){
                it.startAnimation(AnimationUtils.loadAnimation(context,R.anim.pulse_anim))
            }
        }
    }


    open fun prepareItems() {
        mRootView = mRootView!!.findViewById(R.id.mRootView)
        tile0 = mRootView!!.findViewById(R.id.tile0)
        tile1 = mRootView!!.findViewById(R.id.tile1)
        tile2 = mRootView!!.findViewById(R.id.tile2)
        tile3 = mRootView!!.findViewById(R.id.tile3)
        tile4 = mRootView!!.findViewById(R.id.tile4)

        tiles.clear()
        tiles.add(tile0 as UITile)
        tiles.add(tile1 as UITile)
        tiles.add(tile2 as UITile)
        tiles.add(tile3 as UITile)
        tiles.add(tile4 as UITile)

        initUI()
    }


    var buttons: MutableList<MaterialButton> = mutableListOf()


    internal fun initUI() {

    }


    private fun resetKeys() {
        buttons.map {
            (it.tag as? KeyUI)?.let { k ->
                k.key.state = KeyState.None
                it.tag = k
            }
        }
    }

    fun clearSlovo(): Boolean {
        if (keys.size > 0) {

            resetKeys()
            refreshTilesKeys()

            for (i in 0..4) {

                val delay: Long = (i * animStep).toLong()

                Handler(Looper.getMainLooper()).postDelayed({
                    tiles[i].clearKey()
                }, delay)
            }
            keys.clear()

            return true
        } else {
            return false
        }
    }


   /* private fun buttonByKey(key: Key): MaterialButton? {
        var searchedBt: MaterialButton? = null
        buttons.map {
            (it.tag as? KeyUI)?.let { k ->
                if (key.value == k.key.value) {
                    searchedBt = it
                }
            }
        }
        return searchedBt
    }*/

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)
        mRootView?.visibility = visibility
    }

    override fun setOnClickListener(l: OnClickListener?) {
        /* copyImg?.setOnClickListener {
             l?.onClick(it)
         }*/
    }
}

enum class OpenStatus{
    Win,
    Game,
    WrongWord,
    NoComplete
}

