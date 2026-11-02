package com.pain.app

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var intervalInput: EditText
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermission()

        val prefs = getSharedPreferences("pain_settings", MODE_PRIVATE)
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(28, 60, 28, 28)
            setBackgroundColor(0xFF090909.toInt())
        }

        val heading = TextView(this).apply {
            text = "PAIN"
            textSize = 42f
            setTextColor(0xFFD8FF3E.toInt())
            letterSpacing = 0.18f
        }
        val subtitle = TextView(this).apply {
            text = "Choose the pain that moves you forward."
            textSize = 15f
            setTextColor(0xFFFFFFFF.toInt())
            setPadding(0, 10, 0, 38)
        }
        val label = TextView(this).apply {
            text = "REMIND ME EVERY (MINUTES)"
            textSize = 12f
            setTextColor(0xFFBBBBBB.toInt())
        }
        intervalInput = EditText(this).apply {
            setText(prefs.getLong("interval_minutes", 5L).toString())
            setTextColor(0xFFFFFFFF.toInt())
            textSize = 22f
            setSingleLine(true)
            setHintTextColor(0xFF888888.toInt())
            hint = "e.g. 5"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
            setPadding(16, 12, 16, 12)
            setBackgroundColor(0xFF252525.toInt())
        }
        status = TextView(this).apply {
            text = if (prefs.getBoolean("enabled", false)) "REMINDERS ARE ON" else "REMINDERS ARE OFF"
            textSize = 13f
            setTextColor(0xFFD8FF3E.toInt())
            setPadding(0, 24, 0, 24)
        }
        val start = Button(this).apply {
            text = "START REMINDERS"
            setOnClickListener { startReminders() }
        }
        val stop = Button(this).apply {
            text = "STOP REMINDERS"
            setOnClickListener {
                getSharedPreferences("pain_settings", MODE_PRIVATE).edit().putBoolean("enabled", false).apply()
                ReminderScheduler.cancel(this@MainActivity)
                status.text = "REMINDERS ARE OFF"
                Toast.makeText(this@MainActivity, "Reminders stopped", Toast.LENGTH_SHORT).show()
            }
        }
        val preview = Button(this).apply {
            text = "PREVIEW THE TWO CHOICES"
            setOnClickListener { startActivity(Intent(this@MainActivity, ChoiceActivity::class.java)) }
        }

        listOf(heading, subtitle, label, intervalInput, status, start, stop, preview).forEach {
            root.addView(it, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = 12 })
        }
        setContentView(root)
    }

    private fun startReminders() {
        val value = intervalInput.text.toString().toLongOrNull()
        if (value == null || value < 1 || value > 1440) {
            Toast.makeText(this, "Enter a number from 1 to 1440 minutes", Toast.LENGTH_LONG).show()
            return
        }
        getSharedPreferences("pain_settings", MODE_PRIVATE).edit()
            .putLong("interval_minutes", value)
            .putBoolean("enabled", true)
            .apply()
        ReminderScheduler.schedule(this, value)
        status.text = "REMINDERS ARE ON • EVERY $value MIN"
        Toast.makeText(this, "Your first reminder is scheduled", Toast.LENGTH_SHORT).show()
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 7001)
        }
    }
}
