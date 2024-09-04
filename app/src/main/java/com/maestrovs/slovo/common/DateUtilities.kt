package com.maestrovs.slovo.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtilities {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())


    fun getCurrentDateTimeString(): String {
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }


    fun fromStringToDate(dateString: String?): Date? {
        return if (dateString != null) {
            dateFormat.parse(dateString)
        } else {
            null
        }
    }


    fun fromDateToString(date: Date?): String? {
        return date?.let {
            dateFormat.format(it)
        }
    }
}