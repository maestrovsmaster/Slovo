package com.maestrovs.slovo.screens.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity



abstract class BaseFragment : Fragment() {
    protected lateinit var settings: Settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        withActivityOrThrow {
            settings = Settings.get(it)
        }
    }

    protected fun withContextOrThrow(block: (ctx: Context) -> Unit) {
        val ctx = activity ?: context
        if (ctx == null)
            throw IllegalStateException("Context should be available here")
        else
            block(ctx)
    }

    protected fun popBackStack() {
        activity?.onBackPressed()
    }

    protected fun start(
        fragment: androidx.fragment.app.Fragment,
        toBackStack: Boolean = true,
        vararg sharedViews: View
    ) {
        val a = activity
        if (a is BaseActivity)
            a.start(fragment, toBackStack, sharedViews = *sharedViews)
    }

    protected fun start(
        fragment: androidx.fragment.app.Fragment,
        toBackStack: Boolean = true,
        container: Int,
        vararg sharedViews: View
    ) {
        val a = activity
        if (a is BaseActivity)
            a.start(fragment, toBackStack, container, sharedViews = *sharedViews)
    }

    protected fun withActivityOrThrow(block: (activity: FragmentActivity) -> Unit) {
        val activity = activity ?: throw IllegalStateException("Context should be available here")
        block(activity)
    }


   /*  open fun showError(title: String?,message: String){
         val msg = MessagesTranslator.translate(requireContext(),message)
        MaterialDialog(requireContext()).show {
            title(null,title?:"")
            message(null,msg)
            negativeButton {  }
        }
    }*/





}