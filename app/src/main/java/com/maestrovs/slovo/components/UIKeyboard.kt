package com.maestrovs.slovo.components

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.maestrovs.slovo.R
import com.maestrovs.slovo.model.*


class UIKeyboard : FrameLayout {

    var delegate: KeyboardProtocol? = null

    protected var mRootView: View? = null

    protected var row1: LinearLayout? = null
    protected var row2: LinearLayout? = null
    protected var row3: LinearLayout? = null


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

    open val layoutID: Int = R.layout.component_keyboard

    protected open fun init(context: Context) {

        foregroundGravity = Gravity.CENTER_VERTICAL

        val inflater = LayoutInflater.from(context)
        mRootView = inflater.inflate(layoutID, this)
        prepareItems()

        //DUMMY DATA
    }

    open fun prepareItems() {
        mRootView = mRootView!!.findViewById(R.id.mRootView)
        row1 = mRootView!!.findViewById(R.id.row1)
        row2 = mRootView!!.findViewById(R.id.row2)
        row3 = mRootView!!.findViewById(R.id.row3)
        initUI()
    }


    var buttons: MutableList<MaterialButton> = mutableListOf()


    internal fun initUI() {

        KeysList.keysList.map { key ->

            val bt = createButton(key)

            when (key.row) {
                Row.Row1 -> row1!!.addView(bt)
                Row.Row2 -> row2!!.addView(bt)
                Row.Row3 -> row3!!.addView(bt)
            }

            buttons.add(bt)
        }

    }


    fun createButton(keyUI: KeyUI): MaterialButton {
        val button = MaterialButton(context)

        val lp = LinearLayout.LayoutParams(
            0,
            Common.dpToPx(68f, context)
        )//LinearLayout.LayoutParams.WRAP_CONTENT)
        lp.weight = if (keyUI.type == KeyType.Enter ||
            keyUI.type == KeyType.Backspace
        ) {
            1.5f
        } else {
            1f
        }

        lp.leftMargin = Common.dpToPx(2f, context)
        lp.rightMargin = Common.dpToPx(2f, context)
        lp.topMargin = Common.dpToPx(2f, context)
        button.layoutParams = lp

        val spanString = SpannableString(keyUI.key.value)
        spanString.setSpan(StyleSpan(Typeface.BOLD), 0, 1, 0)

        button.text = spanString
        button.setPadding(0, 0, 0, 0)

        button.rippleColor = ContextCompat.getColorStateList(context, R.color.yellow)

        button.tag = keyUI

        drawButtonByState(button, keyUI)

        button.setOnClickListener {
            (it.tag as? KeyUI)?.let { uikey ->
                delegate?.onKey(uikey)
            }
        }

        return button
    }


    private fun drawButtonByState(button: MaterialButton, key: KeyUI) {
        button.backgroundTintList =
            when (key.type) {
                KeyType.Letter -> when (key.key.state) {
                    KeyState.None -> ContextCompat.getColorStateList(context, R.color.gray)
                    KeyState.Wrong -> ContextCompat.getColorStateList(context, R.color.gray_miss)
                    KeyState.Mistake -> ContextCompat.getColorStateList(context, R.color.yellow)
                    KeyState.Right -> ContextCompat.getColorStateList(context, R.color.blue)
                }
                KeyType.Enter -> ContextCompat.getColorStateList(context, R.color.green)
                KeyType.Backspace -> ContextCompat.getColorStateList(context, R.color.red)
            }


        val textColor = when (key.type) {
            KeyType.Letter -> when (key.key.state) {
                KeyState.None -> ContextCompat.getColorStateList(context, R.color.black)
                KeyState.Wrong -> ContextCompat.getColorStateList(context, R.color.white)
                KeyState.Mistake -> ContextCompat.getColorStateList(context, R.color.white)
                KeyState.Right -> ContextCompat.getColorStateList(context, R.color.white)
            }
            KeyType.Enter -> ContextCompat.getColorStateList(context, R.color.white)
            KeyType.Backspace -> ContextCompat.getColorStateList(context, R.color.white)
        }








        button.setTextColor(textColor)
    }

    fun updateKeys(keys: List<Key>?) {
        keys?.let { newKeys ->
            buttons.map {
                (it.tag as? KeyUI)?.let { kUI ->

                    newKeys.map { newK ->
                        if (kUI.key.value == newK.value) {
                            kUI.key.state = newK.state
                        }
                    }
                    it.tag = kUI
                    drawButtonByState(it, kUI)
                }
            }
        }
    }


    fun resetKeys() {
        buttons.map {
            (it.tag as? KeyUI)?.let { k ->
                k.key.state = KeyState.None
                it.tag = k
                drawButtonByState(it, k)
            }

        }
    }


    private fun buttonByKey(key: Key): MaterialButton? {
        var searchedBt: MaterialButton? = null
        buttons.map {
            (it.tag as? KeyUI)?.let { k ->
                if (key.value == k.key.value) {
                    searchedBt = it
                }
            }
        }
        return searchedBt
    }

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

interface KeyboardProtocol {
    fun onKey(keyUI: KeyUI)
}