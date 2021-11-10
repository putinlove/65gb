package com.guldanaev1.a65gb

import java.text.SimpleDateFormat
import java.util.*

fun String?.countMills(): Long {
    val calendar = GregorianCalendar.getInstance()
    calendar.time = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(this)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    if ((calendar.get(Calendar.MONTH) == Calendar.FEBRUARY) && (calendar.get(Calendar.DAY_OF_MONTH) == 29)) {
        val year = calendar.get(Calendar.YEAR)
        if (year.isLeapYear()) {
            calendar.set(
                Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR)
                        + (4 - Calendar.getInstance().get(Calendar.YEAR) % 4)
            )
            calendar.set(Calendar.DAY_OF_MONTH, 29)
            if (!calendar.get(Calendar.YEAR).isLeapYear()) {
                calendar.add(Calendar.YEAR, 4)
            }
            return calendar.timeInMillis
        }
    }
    calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR))
    if (calendar.timeInMillis < System.currentTimeMillis()) {
        calendar.add(Calendar.YEAR, 1)
    }
    return calendar.timeInMillis
}

fun Int.isLeapYear(): Boolean {
    var leap = false
    if (this % 4 == 0) {
        if (this % 100 == 0) {
            leap = this % 400 == 0
        } else
            leap = true
    } else
        leap = false
    return leap
}
