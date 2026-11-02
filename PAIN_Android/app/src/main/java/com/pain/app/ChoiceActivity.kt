package com.pain.app

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class ChoiceActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawableResource(android.R.color.transparent)
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.setDimAmount(0.72f)
        window.setGravity(Gravity.BOTTOM)
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            (resources.displayMetrics.heightPixels * 0.54f).toInt()
        )

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(28, 28, 28, 34)
            background = GradientDrawable().apply {
                setColor(Color.rgb(15, 15, 15))
                cornerRadii = floatArrayOf(28f,28f,28f,28f,0f,0f,0f,0f)
            }
        }

        val eyebrow = TextView(this).apply {
            text = "PAIN IS PART OF THE PROCESS"
            setTextColor(Color.rgb(190, 190, 190))
            textSize = 11f
            letterSpacing = 0.15f
            gravity = Gravity.CENTER
        }
        val title = TextView(this).apply {
            text = "Choose your pain."
            setTextColor(Color.WHITE)
            textSize = 27f
            typeface = Typeface.create("sans-serif", Typeface.BOLD)
            gravity = Gravity.CENTER
            setPadding(0, 18, 0, 26)
        }
        root.addView(eyebrow)
        root.addView(title)
        root.addView(makeButton("THE PAIN OF STAYING WHERE YOU ARE", false) {
            chooseGrowth(false)
        }, matchWrap())
        root.addView(makeButton("THE PAIN OF GROWTH", true) {
            chooseGrowth(true)
        }, matchWrap().apply { topMargin = 14 })

        val note = TextView(this).apply {
            text = "Your choice decides when you hear from me again."
            setTextColor(Color.LTGRAY)
            textSize = 12f
            gravity = Gravity.CENTER
            setPadding(0, 24, 0, 0)
        }
        root.addView(note)
        setContentView(root)
    }

    private fun matchWrap() = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
    )

    private fun makeButton(label: String, highlighted: Boolean, action: () -> Unit): Button {
        return Button(this).apply {
            text = label
            textSize = 13f
            isAllCaps = false
            setTextColor(if (highlighted) Color.BLACK else Color.WHITE)
            background = GradientDrawable().apply {
                cornerRadius = 18f
                setColor(if (highlighted) Color.rgb(216, 255, 62) else Color.rgb(43, 43, 43))
            }
            setPadding(12, 18, 12, 18)
            setOnClickListener { action() }
        }
    }

    private fun chooseGrowth(growth: Boolean) {
        val prefs = getSharedPreferences("pain_settings", MODE_PRIVATE)
        val interval = prefs.getLong("interval_minutes", 5L)
        // Growth pauses reminders for two hours. Staying resumes the user's interval.
        ReminderScheduler.schedule(this, if (growth) 120L else interval)
        finish()
    }
}
