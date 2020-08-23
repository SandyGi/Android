package com.sandy.chalkboard.utils

import java.text.SimpleDateFormat
import java.util.*

class CommonMethods {
    companion object{
        fun getDate(milliSeconds: Long, dateFormat: String?): String? {
            // Create a DateFormatter object for displaying date in specified format.
            val formatter = SimpleDateFormat(dateFormat)

            // Create a calendar object that will convert the date and time value in milliseconds to date.
            val calendar: Calendar = Calendar.getInstance()
            calendar.timeInMillis = milliSeconds
            return formatter.format(calendar.time)
        }
    }
}