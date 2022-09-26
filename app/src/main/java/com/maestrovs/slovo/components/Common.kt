package com.maestrovs.slovo.components

import android.content.Context
import android.util.DisplayMetrics

object Common {

    fun dpToPx(dp: Float, context: Context): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return Math.round(dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT) * 100) / 100
    }
}