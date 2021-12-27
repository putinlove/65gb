package com.guldanaev1.presentation.receivers

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.guldanaev1.presentation.utils.countMills
import com.guldanaev1.presentation.R
import com.guldanaev1.presentation.view.activity.MainActivity

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notifyBody = intent.getStringExtra(NAME).toString()
        val id = intent.getStringExtra(ID).toString()
        val date = intent.getStringExtra(DATE)
        val intentMainActivity = Intent(context, MainActivity::class.java).apply {
            putExtra(ID, id)
        }
        createNotification(context, intentMainActivity, notifyBody, id)
        createNotificationChannel(context)
        val alarmMgr: AlarmManager? =
            context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val alarmIntent = PendingIntent.getBroadcast(
            context,
            id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val timeBeforeBirthdayInMills: Long = date.countMills()
        alarmMgr?.set(
            AlarmManager.RTC_WAKEUP,
            timeBeforeBirthdayInMills, alarmIntent
        )
    }

    private fun createNotification(
        context: Context,
        intentMainActivity: Intent,
        notifyBody: String,
        id: String
    ) {
        val pendingIntent = PendingIntent.getActivity(
            context,
            id.toInt(),
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
        NotificationManagerCompat.from(context).notify(id.toInt(), notification.build())
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                context.getString(R.string.birthday_notification_channel_id),
                context.getString(R.string.notification_title),
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        const val NAME = "name"
        const val ID = "id"
        const val DATE = "date"
    }
}
