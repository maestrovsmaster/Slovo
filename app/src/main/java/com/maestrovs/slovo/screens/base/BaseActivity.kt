package com.maestrovs.slovo.screens.base

import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    protected abstract val fragmentContainerId: Int

    @CallSuper
    fun start(
        fragment: androidx.fragment.app.Fragment,
        toBackStack: Boolean = false,
        container: Int = fragmentContainerId,
        vararg sharedViews: View
    ) {
        supportFragmentManager
            .beginTransaction()
            .apply {
                if (toBackStack)
                    addToBackStack(fragment.javaClass.name)
                replace(container, fragment)
            }.commitAllowingStateLoss()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(fragmentContainerId)
        if (fragment !is IOnBackPressed || fragment.onBackPressed())
            super.onBackPressed()
    }
}