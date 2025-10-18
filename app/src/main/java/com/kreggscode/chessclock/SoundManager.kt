package com.kreggscode.chessclock

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

/**
 * Manages all audio and haptic feedback for the chess clock
 */
class SoundManager(private val context: Context) {
    
    private var soundPool: SoundPool? = null
    private var toneGenerator: ToneGenerator? = null
    private val vibrator: Vibrator by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }
    
    // Sound IDs (would be loaded from actual sound files in production)
    private var clickSoundId: Int = -1
    private var warningSoundId: Int = -1
    private var gameOverSoundId: Int = -1
    
    // Settings
    var isSoundEnabled = true
    var isVibrationEnabled = true
    var isLowTimeWarningEnabled = true
    
    init {
        initializeSoundPool()
        initializeToneGenerator()
    }
    
    private fun initializeSoundPool() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        
        soundPool = SoundPool.Builder()
            .setMaxStreams(3)
            .setAudioAttributes(audioAttributes)
            .build()
    }
    
    private fun initializeToneGenerator() {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 80)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * Play click sound when player taps their timer
     */
    fun playClickSound() {
        if (!isSoundEnabled) return
        
        toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 50)
    }
    
    /**
     * Play warning sound when time is running low
     */
    fun playWarningSound() {
        if (!isSoundEnabled || !isLowTimeWarningEnabled) return
        
        toneGenerator?.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 100)
    }
    
    /**
     * Play critical sound when time is very low
     */
    fun playCriticalSound() {
        if (!isSoundEnabled || !isLowTimeWarningEnabled) return
        
        toneGenerator?.startTone(ToneGenerator.TONE_CDMA_EMERGENCY_RINGBACK, 150)
    }
    
    /**
     * Play game over sound
     */
    fun playGameOverSound() {
        if (!isSoundEnabled) return
        
        toneGenerator?.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT, 300)
    }
    
    /**
     * Vibrate on click
     */
    fun vibrateClick() {
        if (!isVibrationEnabled) return
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(30)
        }
    }
    
    /**
     * Vibrate on warning
     */
    fun vibrateWarning() {
        if (!isVibrationEnabled || !isLowTimeWarningEnabled) return
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createWaveform(longArrayOf(0, 100, 50, 100), -1)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(200)
        }
    }
    
    /**
     * Vibrate on game over
     */
    fun vibrateGameOver() {
        if (!isVibrationEnabled) return
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createWaveform(
                    longArrayOf(0, 100, 100, 100, 100, 100),
                    -1
                )
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(500)
        }
    }
    
    /**
     * Release all resources
     */
    fun release() {
        soundPool?.release()
        soundPool = null
        toneGenerator?.release()
        toneGenerator = null
    }
}
