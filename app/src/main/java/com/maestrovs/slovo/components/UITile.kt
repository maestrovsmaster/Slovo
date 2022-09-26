package com.maestrovs.slovo.components

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.maestrovs.slovo.R
import com.maestrovs.slovo.model.*

class UITile : FrameLayout {


    protected var mRootView: View? = null

    protected  var flipper: com.maestrovs.slovo.components.flip.FlipperLayout? = null

   // protected var buttonFront: MaterialButton? = null

    protected var buttonFront: ImageView? = null
    protected var textFront: TextView? = null

    protected var buttonRear: ImageView? = null
    protected var textRear: TextView? = null


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

    open val layoutID: Int = R.layout.component_tile

    protected open fun init(context: Context) {

        foregroundGravity = Gravity.CENTER_VERTICAL

        val inflater = LayoutInflater.from(context)
        mRootView =
            inflater.inflate(layoutID, this)
        prepareItems()

        //DUMMY DATA
    }

    private var key: Key? = null

    fun setKey(key: Key?) {
        this.key = key
        renderTile()
    }

    fun getKey() = key

    fun clearKey(){
        this.key = null
        renderTile()
        flipper!!.next()
    }

    fun flip(){
        flipper!!.next()
    }

    open fun prepareItems() {
        mRootView = mRootView!!.findViewById(R.id.mRootView)
        flipper = mRootView!!.findViewById(R.id.flipper)
        buttonFront = mRootView!!.findViewById(R.id.buttonFront)
        buttonRear = mRootView!!.findViewById(R.id.buttonRear)
        textRear = mRootView!!.findViewById(R.id.textRear)
        textFront= mRootView!!.findViewById(R.id.textFront)

        initUI()
    }


    var buttons: MutableList<MaterialButton> = mutableListOf()


    internal fun initUI() {
        renderTile()
    }


    fun renderTile() {


        val bg1 = ContextCompat.getColorStateList(context, R.color.white)

        val stroke1 = if (key == null) {
            ContextCompat.getColorStateList(context, R.color.stroke_gray)
        } else {
            ContextCompat.getColorStateList(context, R.color.stroke_gray2)
        }

        val txt = if (key == null) {
            ""
        } else {
            key!!.value
        }

        val strokeBg2 = if (key == null) {
            ContextCompat.getColorStateList(context, R.color.gray)
        } else {
            when (key!!.state) {
                KeyState.None -> ContextCompat.getColorStateList(context, R.color.gray)
                KeyState.Wrong -> ContextCompat.getColorStateList(context, R.color.black)
                KeyState.Mistake -> ContextCompat.getColorStateList(context, R.color.yellow)
                KeyState.Right -> ContextCompat.getColorStateList(context, R.color.blue)
            }
        }


        val textColor2 =
            if (key == null) {
                ContextCompat.getColorStateList(context, R.color.gray)
            } else {
                ContextCompat.getColorStateList(context, R.color.white)
            }

        buttonFront!!.backgroundTintList = bg1
        buttonRear!!.backgroundTintList = strokeBg2

      // buttonRear!!.background = ContextCompat.getDrawable(context,R.drawable.ic_orn_green)

      //  buttonRear!!.setBackgroundResource(R.drawable.ic_orn_green)

       // buttonRear!!.setBackgroundDrawable()

       // buttonFront!!.setTextColor(ContextCompat.getColorStateList(context, R.color.gray_miss))
     //   buttonRear!!.setTextColor(textColor2)

        var iconRear =
            if (key != null) {
                when (key!!.state) {
                    KeyState.None -> R.drawable.ic_light_gray1
                    KeyState.Wrong -> R.drawable.ic_gray1
                    KeyState.Mistake -> R.drawable.yellow
                    KeyState.Right -> R.drawable.blue
                }
            } else {
                R.drawable.ic_light_gray1
            }


        buttonRear!!.setImageResource(iconRear)

        textFront!!.text = txt

        textRear!!.text = txt

       // buttonFront!!.strokeColor = stroke1

        buttonFront!!.setImageResource(R.drawable.ic_light)

     //   buttonRear!!.strokeColor = strokeBg2
    }






       /* fun setState(key: Key) {
            buttonByKey(key)?.let { /*searchedBt ->
            buttons.filter {
                it.id == searchedBt.id
            }.forEach {
                it.tag = key
                drawButtonByState(it, key)
            }*/

                drawButtonByState(it, key)
            }
        }*/

        fun resetKeys() {
            buttons.map {
                (it.tag as? KeyUI)?.let { k ->
                    k.key.state = KeyState.None
                    it.tag = k
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

