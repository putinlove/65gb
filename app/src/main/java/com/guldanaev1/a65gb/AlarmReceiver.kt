package com.guldanaev1.a65gb

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val notifyBody: String = intent.getStringExtra("name").toString()
        val id: String? = intent.getStringExtra("id")
        val date: String? = intent.getStringExtra("date")
        val intentMainActivity = Intent(context, MainActivity::class.java).apply {
            putExtra("id", id)
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intentMainActivity,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification = NotificationCompat.Builder(
            context,
            context.getString(R.string.birthday_notification_channel_id)
        )
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(
                context
                    .getString(R.string.notification_title)
            )
            .setContentText(notifyBody)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        NotificationManagerCompat.from(context).notify(1, notification.build())

        val alarmMgr: AlarmManager? =
            context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val alarmIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val timeBeforeBirthdayInMills: Long = date.countMills()
        alarmMgr?.set(
            AlarmManager.RTC_WAKEUP,
            timeBeforeBirthdayInMills, alarmIntent
        )
    }
}
