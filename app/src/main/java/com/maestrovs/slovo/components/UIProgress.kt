package com.maestrovs.slovo.components

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.maestrovs.slovo.R


class UIProgress : ProgressBar {

    private var reverse: Boolean = false
    private var customTextSize: Float = 14f

    var active: Boolean = true
        set(value) {
            field = value

            initBackground()
        }

    constructor(context: Context) : super(context) {
        initNonStyle(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initNonStyle(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun initNonStyle(context: Context, attrs: AttributeSet?) {


        init(context, attrs)
    }


    private fun init(context: Context, attrs: AttributeSet?) {
        this.minimumHeight = Common.dpToPx(50f, context)



        setPadding(Common.dpToPx(0f, context))

      //  progressTintList = ColorStateList.valueOf(context.getColor(R.color.blue))
        background = ContextCompat.getDrawable(context, R.color.transparent)


        initBackground()
    }


    private fun initBackground() {


        val animatedVector =
            AnimatedVectorDrawableCompat.create(context, R.drawable.avd_anim)

        setIndeterminateDrawableTiled(animatedVector)
        progressDrawable = animatedVector
        isIndeterminate = true

        val mainHandler = Handler(Looper.getMainLooper())
        animatedVector!!.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable) {
                mainHandler.post(Runnable { animatedVector.start() })
            }
        })
        animatedVector.start()



    }
}
