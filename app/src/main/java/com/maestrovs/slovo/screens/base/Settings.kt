package com.maestrovs.slovo.screens.base

import android.app.Application
import android.content.Context


object Settings : APreferences("localProfile") {



    private var ctx: Application? = null

    private val setBlock: (key: String, value: Any) -> Unit = { key, value ->
        ctx.putAtomic(key, value)
    }

    private val getBlock: (key: String, default: Any) -> Any = { key, default ->
        ctx.getAtomic(key, default)
    }

    fun get(context: Context): Settings {
        if (ctx == null)
            ctx = context.applicationContext as Application

        return this
    }

    fun get(application: Application): Settings {
        if (ctx == null)
            ctx = application

        return this
    }

    fun with(context: Context, block: (profile: Settings) -> Unit) {
        block(get(context))
    }
}