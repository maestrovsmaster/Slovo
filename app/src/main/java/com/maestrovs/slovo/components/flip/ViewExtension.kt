package com.maestrovs.slovo.components.flip

import android.view.View
import com.maestrovs.slovo.components.flip.animation.MtxRotationAnimation

/**
 * Extension method to rotate a view perspectively.
 */
fun View.rotatePerspectively(axis: Int, deg: Int) {
    val animation = MtxRotationAnimation(axis, deg, deg, 1)
    startAnimation(animation)
}