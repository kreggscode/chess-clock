package com.kreggscode.chessclock

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kreggscode.chessclock.databinding.ActivitySetupBinding

class SetupActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySetupBinding
    private var selectedMinutes = 5
    private var selectedButton: MaterialButton? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen before super.onCreate()
        installSplashScreen()
        
        super.onCreate(savedInstanceState)
        binding = ActivitySetupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Load last used time
        val prefs = getSharedPreferences(SettingsActivity.PREFS_NAME, Context.MODE_PRIVATE)
        selectedMinutes = prefs.getInt(SettingsActivity.KEY_TIME_MINUTES, 5)
        
        setupTimeButtons()
        setupButtons()
    }
    
    private fun setupTimeButtons() {
        val timeButtons = mapOf(
            binding.time1min to 1,
            binding.time3min to 3,
            binding.time5min to 5,
            binding.time10min to 10,
            binding.time15min to 15,
            binding.time30min to 30,
            binding.time45min to 45,
            binding.time60min to 60,
            binding.time90min to 90
        )
        
        timeButtons.forEach { (button, minutes) ->
            button.setOnClickListener {
                selectTime(button, minutes)
            }
            
            // Pre-select the saved time
            if (minutes == selectedMinutes) {
                selectTime(button, minutes)
            }
        }
        
        // Custom time button
        binding.customTimeButton.setOnClickListener {
            showCustomTimeDialog()
        }
    }
    
    private fun selectTime(button: MaterialButton, minutes: Int) {
        // Deselect previous button
        selectedButton?.let {
            it.strokeWidth = 2
            it.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
        }
        
        // Select new button
        selectedMinutes = minutes
        selectedButton = button
        button.strokeWidth = 4
        button.strokeColor = ContextCompat.getColorStateList(this, R.color.accent)
        
        // Save selection
        val prefs = getSharedPreferences(SettingsActivity.PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(SettingsActivity.KEY_TIME_MINUTES, selectedMinutes).apply()
    }
    
    private fun setupButtons() {
        binding.startButton.setOnClickListener {
            startGame()
        }
        
        binding.settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun startGame() {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra(EXTRA_TIME_MINUTES, selectedMinutes)
        startActivity(intent)
    }
    
    private fun showCustomTimeDialog() {
        val input = EditText(this).apply {
            inputType = InputType.TYPE_CLASS_NUMBER
            hint = getString(R.string.custom_time_hint)
            setText(selectedMinutes.toString())
            setPadding(64, 32, 64, 32)
        }
        
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.custom_time_title)
            .setMessage(R.string.custom_time_message)
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val minutes = input.text.toString().toIntOrNull()
                if (minutes != null && minutes in 1..1440) {
                    // Deselect all preset buttons
                    selectedButton?.let {
                        it.strokeWidth = 2
                        it.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent))
                    }
                    selectedButton = null
                    
                    // Update custom button to show selected time
                    selectedMinutes = minutes
                    binding.customTimeButton.text = "$minutes min"
                    binding.customTimeButton.strokeWidth = 4
                    
                    // Save selection
                    val prefs = getSharedPreferences(SettingsActivity.PREFS_NAME, Context.MODE_PRIVATE)
                    prefs.edit().putInt(SettingsActivity.KEY_TIME_MINUTES, selectedMinutes).apply()
                } else {
                    MaterialAlertDialogBuilder(this)
                        .setTitle("Invalid Time")
                        .setMessage("Please enter a time between 1 and 1440 minutes (24 hours)")
                        .setPositiveButton("OK", null)
                        .show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    override fun onResume() {
        super.onResume()
        // Reload time in case it changed in settings
        val prefs = getSharedPreferences(SettingsActivity.PREFS_NAME, Context.MODE_PRIVATE)
        val newTime = prefs.getInt(SettingsActivity.KEY_TIME_MINUTES, 5)
        if (newTime != selectedMinutes) {
            selectedMinutes = newTime
            // Re-highlight the correct button
            setupTimeButtons()
        }
    }
    
    companion object {
        const val EXTRA_TIME_MINUTES = "extra_time_minutes"
    }
}
