package com.pain.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED) return
        val prefs = context.getSharedPreferences("pain_settings", Context.MODE_PRIVATE)
        if (prefs.getBoolean("enabled", false)) {
            ReminderScheduler.schedule(context, prefs.getLong("interval_minutes", 5L))
        }
    }
}
