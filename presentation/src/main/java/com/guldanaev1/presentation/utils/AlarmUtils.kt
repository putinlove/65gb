package com.guldanaev1.presentation.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.guldanaev1.presentation.R
import com.guldanaev1.presentation.receivers.AlarmReceiver
import com.guldanaev1.presentation.receivers.AlarmReceiver.Companion.DATE
import com.guldanaev1.presentation.receivers.AlarmReceiver.Companion.ID
import com.guldanaev1.presentation.receivers.AlarmReceiver.Companion.NAME

object AlarmUtils {
    fun setupAlarm(
        context: Context,
        contactName: String?,
        contactId: String,
        contactBirthday: String?
    ) {
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notifyBody = context.getString(R.string.notification_text, contactName)
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(NAME, notifyBody)
            putExtra(ID, contactId)
            putExtra(DATE, contactBirthday)
        }
        val existingIntent = PendingIntent.getBroadcast(
            context,
            contactId.toInt(),
            intent,
            PendingIntent.FLAG_NO_CREATE
        )
        if (existingIntent == null) {
            val alarmIntent = PendingIntent.getBroadcast(
                context,
                contactId.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            val timeBeforeBirthdayInMills: Long = contactBirthday.countMills()
            alarmMgr.set(
                AlarmManager.RTC_WAKEUP,
                timeBeforeBirthdayInMills, alarmIntent
            )
        }
    }

    fun cancelAlarm(context: Context, contactId: String) {
        val alarmMgr: AlarmManager? = null
        val intent = Intent(context, AlarmReceiver::class.java)
        val alarmIntent = PendingIntent.getBroadcast(
            context,
            contactId.toInt(),
            intent,
            0
        )
        alarmMgr?.cancel(alarmIntent)
        alarmIntent.cancel()
    }

    fun setSwitchState(context: Context, contactId: String): Boolean {
        val intent = Intent(context, AlarmReceiver::class.java)
        val existingIntent = PendingIntent.getBroadcast(
            context,
            contactId.toInt(),
            intent,
            PendingIntent.FLAG_NO_CREATE
        )
        return existingIntent != null
    }
}
