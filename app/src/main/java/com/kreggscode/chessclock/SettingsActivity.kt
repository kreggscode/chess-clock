package com.kreggscode.chessclock

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kreggscode.chessclock.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var prefs: SharedPreferences
    
    companion object {
        const val PREFS_NAME = "ChessClockPrefs"
        const val KEY_TIME_MINUTES = "time_minutes"
        const val KEY_SOUND_ENABLED = "sound_enabled"
        const val KEY_VIBRATION_ENABLED = "vibration_enabled"
        const val KEY_LOW_TIME_WARNING = "low_time_warning"
        
        const val DEFAULT_TIME_MINUTES = 5
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        
        setupToolbar()
        setupAudioSettings()
    }
    
    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }
    
    private fun setupAudioSettings() {
        // Load saved settings
        binding.soundSwitch.isChecked = prefs.getBoolean(KEY_SOUND_ENABLED, true)
        binding.vibrationSwitch.isChecked = prefs.getBoolean(KEY_VIBRATION_ENABLED, true)
        binding.lowTimeWarningSwitch.isChecked = prefs.getBoolean(KEY_LOW_TIME_WARNING, true)
        
        // Set up listeners
        binding.soundSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean(KEY_SOUND_ENABLED, isChecked).apply()
        }
        
        binding.vibrationSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean(KEY_VIBRATION_ENABLED, isChecked).apply()
        }
        
        binding.lowTimeWarningSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean(KEY_LOW_TIME_WARNING, isChecked).apply()
        }
    }
}
