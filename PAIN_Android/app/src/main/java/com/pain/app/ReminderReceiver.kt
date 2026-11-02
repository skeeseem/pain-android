package com.pain.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val prefs = context.getSharedPreferences("pain_settings", Context.MODE_PRIVATE)
        if (!prefs.getBoolean("enabled", false)) return

        createChannel(context)
        val openChoice = Intent(context, ChoiceActivity::class.java)
        val contentIntent = PendingIntent.getActivity(
            context, 4102, openChoice,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(context, "pain_reminders")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("PAIN")
            .setContentText("Which pain are you choosing?")
            .setStyle(NotificationCompat.BigTextStyle().bigText(
                "The pain of staying where you are — or the pain of growth?"
            ))
            .setContentIntent(contentIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(4103, notification)
        } catch (_: SecurityException) {
            // Notification permission was not granted.
        }
    }

    private fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "pain_reminders", "PAIN reminders", NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "Your recurring growth reminder" }
            context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }
}
