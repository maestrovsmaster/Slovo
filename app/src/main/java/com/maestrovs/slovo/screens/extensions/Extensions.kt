package com.maestrovs.slovo.screens.extensions

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

fun View.hideSoftKeyboard() {
    (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(windowToken, 0)
}

fun applyFont(context: Context, root: View?, fontName: kotlin.String?) {
    try {
        if (root is ViewGroup) {
            val viewGroup = root
            for (i in 0 until viewGroup.childCount)
                applyFont(
                    context,
                    viewGroup.getChildAt(i),
                    fontName
                )
        } else if (root is TextView) {
            root.setTypeface(
                Typeface.createFromAsset(
                    context.getAssets(),
                    fontName
                ), Typeface.BOLD
            )
            Log.d(
                "ProjectName",
                String.format(
                    "Apply %s font for %s view",
                    fontName,
                    root
                )
            )
        }
    } catch (e: Exception) {
        Log.e(
            "ProjectName",
            String.format(
                "Error occured when trying to apply %s font for %s view",
                fontName,
                root
            )
        )
        e.printStackTrace()
    }
}

fun Disposable.addTo(compositeDisposable: CompositeDisposable){
    compositeDisposable.add(this)
}





