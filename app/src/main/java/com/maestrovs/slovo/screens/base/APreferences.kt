package com.maestrovs.slovo.screens.base

import android.content.Context
import android.content.SharedPreferences
import androidx.databinding.ObservableField

abstract class APreferences(val preferencesName: String) {

    protected var preferences: SharedPreferences? = null

    protected inline fun Context?.withPreferences(block: SharedPreferences.() -> Unit) {
        var pref = preferences
        if (pref == null && this != null) {
            pref = getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
            preferences = pref
        }
        pref?.block()
    }

    protected inline fun Context?.withEditor(block: SharedPreferences.Editor.(preferences: SharedPreferences) -> Unit) =
            withPreferences {
                val editor = edit()
                block(editor, this)
                editor.apply()
            }

    protected inline fun <reified T> Context?.putAtomic(key: String, value: T) = this.withEditor {
        when (value) {
            is Boolean -> putBoolean(key, value)
            is String  -> putString(key, value)
            is Float   -> putFloat(key, value)
            is Long    -> putLong(key, value)
            is Int     -> putInt(key, value)

            else       -> throw IllegalArgumentException("This type can't be saved to Preferences")
        }
    }

    @Suppress("IMPLICIT_CAST_TO_ANY")
    protected inline fun <reified T> Context?.getAtomic(key: String, default: T): T {
        var value = default
        this.withPreferences {
            value = when (default) {
                is Boolean -> getBoolean(key, default)
                is String  -> getString(key, default)
                is Float   -> getFloat(key, default)
                is Long    -> getLong(key, default)
                is Int     -> getInt(key, default)

                else       -> throw IllegalArgumentException("This type can't be restored from Preferences")
            } as T
        }
        return value
    }
}

@Suppress("UNCHECKED_CAST")
class ObservablePreferenceField<T>(
        getBlock: (key: String, default: Any) -> Any,
        private val setBlock: (key: String, value: Any) -> Unit,
        private val key: String,
        default: T) : ObservableField<T>(getBlock(key, default as Any) as T) {

    override fun get(): T = super.get()!!

    override fun set(value: T) {
        setBlock(key, value as Any)
        super.set(value)
    }
}