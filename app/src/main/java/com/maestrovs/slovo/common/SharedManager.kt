package com.maestrovs.slovo.common

import android.content.Context
import android.util.Log


object SharedManager {

    const val SLOVO_PREFS = "ALR_DRIVER_PREFS"
    const val ACCEPT_AGREEMENT = "ACCEPT_AGREEMENT"

    const val COUNT_OF_LOCATION_DENIEDS = "COUNT_OF_LOCATION_DENIEDS"



    fun initPrefs(context: Context) {
        context.getSharedPreferences(SLOVO_PREFS, Context.MODE_PRIVATE)
    }

    fun writeStringOption(context: Context, key: String?, value: String?) {
        val editor =
            context.getSharedPreferences(SLOVO_PREFS, Context.MODE_PRIVATE).edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun removeOption(context: Context, key: String?) {
        val editor =
            context.getSharedPreferences(SLOVO_PREFS, Context.MODE_PRIVATE).edit()
        editor.remove(key)
        editor.apply()
    }

    fun readStringOptions(context: Context, key: String?): String? {
        val prefs = context.getSharedPreferences(SLOVO_PREFS, Context.MODE_PRIVATE)
        return prefs.getString(key, null)
    }

    fun writeIntOption(context: Context, key: String?, value: Int) {
        val editor =
            context.getSharedPreferences(SLOVO_PREFS, Context.MODE_PRIVATE).edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun readIntOption(context: Context, key: String?): Int {
        val prefs = context.getSharedPreferences(SLOVO_PREFS, Context.MODE_PRIVATE)
        return prefs.getInt(key, -1)
    }

    fun writeBoolOption(context: Context, key: String?, value: Boolean) {
        val editor =
            context.getSharedPreferences(SLOVO_PREFS, Context.MODE_PRIVATE).edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun readBoolOption(context: Context, key: String?): Boolean {
        val prefs = context.getSharedPreferences(SLOVO_PREFS, Context.MODE_PRIVATE)
        return prefs.getBoolean(key, false)
    }


    fun getIsAcceptAgreement(context: Context) =
        readBoolOption(context, ACCEPT_AGREEMENT)

    fun setAcceptAgreement(context: Context, accept: Boolean) =
        writeBoolOption(context, ACCEPT_AGREEMENT, accept)


    fun addLocationDenial(context: Context) {
        var currentLocationDenials = getLocationDenials(context)
        currentLocationDenials += 1
        writeIntOption(context, COUNT_OF_LOCATION_DENIEDS, currentLocationDenials)
    }

    private fun getLocationDenials(context: Context): Int {
        var currentLocationDenials = readIntOption(context, COUNT_OF_LOCATION_DENIEDS)
        if (currentLocationDenials == -1) currentLocationDenials = 0
        return currentLocationDenials
    }

    fun canRequestLocationAccess(context: Context): Boolean {
        var currentLocationDenials = getLocationDenials(context)
        return currentLocationDenials < 2
    }



    fun writeWordsList(words: HashSet<String>?,context: Context){
        Log.d("Game","options: writeWordsList=$words")
        val editor =
            context.getSharedPreferences(SLOVO_PREFS, Context.MODE_PRIVATE).edit()
        editor.putStringSet("words", words);
        editor.commit();
    }

    fun readWordsList(context: Context): MutableSet<String>? {
        val prefs = context.getSharedPreferences(SLOVO_PREFS, Context.MODE_PRIVATE)
        return prefs.getStringSet("words", null)
    }

    fun writeLastSlovo(slovo: String?,context: Context){
        Log.d("Game","options: writeLastSlovo=$slovo")
        writeStringOption(context,"slovo",slovo)
    }

    fun readLastSlovo(context: Context): String?{
        val slovo = readStringOptions(context,"slovo")
        Log.d("Game","options: readLastSlovo=$slovo")
        return slovo
    }


    /*fun writetWord(word: String, tag: String, context: Context){
        writeStringOption(context,tag,word)
    }

    fun readWord(context: Context): String?{
        return readStringOptions(context,"slovo")
    }*/


}