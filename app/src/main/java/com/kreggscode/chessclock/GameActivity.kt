package com.kreggscode.chessclock

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PowerManager
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.kreggscode.chessclock.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityGameBinding
    private lateinit var soundManager: SoundManager
    private var wakeLock: PowerManager.WakeLock? = null
    
    // Game state
    private enum class GameState {
        RUNNING, PAUSED, FINISHED
    }
    
    private enum class ActivePlayer {
        WHITE, BLACK
    }
    
    private var gameState = GameState.RUNNING
    private var activePlayer = ActivePlayer.WHITE
    
    // Timers
    private var whiteTimer: CountDownTimer? = null
    private var blackTimer: CountDownTimer? = null
    private var whiteTimeMillis: Long = 0
    private var blackTimeMillis: Long = 0
    
    // Warning thresholds
    private val WARNING_THRESHOLD_MS = 60_000L
    private val CRITICAL_THRESHOLD_MS = 10_000L
    
    private var whiteWarningPlayed = false
    private var blackWarningPlayed = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupFullScreen()
        acquireWakeLock()
        
        soundManager = SoundManager(this)
        loadSettings()
        
        // Get time from intent
        val timeMinutes = intent.getIntExtra(SetupActivity.EXTRA_TIME_MINUTES, 5)
        whiteTimeMillis = timeMinutes * 60_000L
        blackTimeMillis = timeMinutes * 60_000L
        
        setupClickListeners()
        updateUI()
        
        // Start with white's turn
        startWhiteTimer()
    }
    
    private fun setupFullScreen() {
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            )
        }
    }
    
    private fun acquireWakeLock() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "ChessClock::GameWakeLock"
        )
        wakeLock?.acquire(10*60*60*1000L)
    }
    
    private fun loadSettings() {
        val prefs = getSharedPreferences(SettingsActivity.PREFS_NAME, Context.MODE_PRIVATE)
        soundManager.isSoundEnabled = prefs.getBoolean(SettingsActivity.KEY_SOUND_ENABLED, true)
        soundManager.isVibrationEnabled = prefs.getBoolean(SettingsActivity.KEY_VIBRATION_ENABLED, true)
        soundManager.isLowTimeWarningEnabled = prefs.getBoolean(SettingsActivity.KEY_LOW_TIME_WARNING, true)
    }
    
    private fun setupClickListeners() {
        // White player area - tap to switch to black
        binding.whitePlayerArea.setOnClickListener {
            if (gameState == GameState.RUNNING && activePlayer == ActivePlayer.WHITE) {
                switchToBlack()
            }
        }
        
        // Black player area - tap to switch to white
        binding.blackPlayerArea.setOnClickListener {
            if (gameState == GameState.RUNNING && activePlayer == ActivePlayer.BLACK) {
                switchToWhite()
            }
        }
        
        // Pause/Resume button
        binding.pauseButton.setOnClickListener {
            togglePause()
        }
        
        // Reset button
        binding.resetButton.setOnClickListener {
            showResetDialog()
        }
        
        // Back button
        binding.backButton.setOnClickListener {
            showExitDialog()
        }
    }
    
    private fun startWhiteTimer() {
        blackTimer?.cancel()
        
        whiteTimer = object : CountDownTimer(whiteTimeMillis, 100) {
            override fun onTick(millisUntilFinished: Long) {
                whiteTimeMillis = millisUntilFinished
                updateWhiteTime()
                checkWhiteWarnings()
            }
            
            override fun onFinish() {
                whiteTimeMillis = 0
                updateWhiteTime()
                gameOver(ActivePlayer.BLACK)
            }
        }.start()
        
        activePlayer = ActivePlayer.WHITE
        updateUI()
    }
    
    private fun startBlackTimer() {
        whiteTimer?.cancel()
        
        blackTimer = object : CountDownTimer(blackTimeMillis, 100) {
            override fun onTick(millisUntilFinished: Long) {
                blackTimeMillis = millisUntilFinished
                updateBlackTime()
                checkBlackWarnings()
            }
            
            override fun onFinish() {
                blackTimeMillis = 0
                updateBlackTime()
                gameOver(ActivePlayer.WHITE)
            }
        }.start()
        
        activePlayer = ActivePlayer.BLACK
        updateUI()
    }
    
    private fun switchToWhite() {
        soundManager.playClickSound()
        soundManager.vibrateClick()
        startWhiteTimer()
        animatePlayerSwitch()
    }
    
    private fun switchToBlack() {
        soundManager.playClickSound()
        soundManager.vibrateClick()
        startBlackTimer()
        animatePlayerSwitch()
    }
    
    private fun togglePause() {
        when (gameState) {
            GameState.RUNNING -> {
                gameState = GameState.PAUSED
                whiteTimer?.cancel()
                blackTimer?.cancel()
                binding.pauseButton.setIconResource(R.drawable.ic_play)
                binding.pausedOverlay.visibility = View.VISIBLE
                binding.pausedText.visibility = View.VISIBLE
            }
            GameState.PAUSED -> {
                gameState = GameState.RUNNING
                binding.pauseButton.setIconResource(R.drawable.ic_pause)
                binding.pausedOverlay.visibility = View.GONE
                binding.pausedText.visibility = View.GONE
                if (activePlayer == ActivePlayer.WHITE) {
                    startWhiteTimer()
                } else {
                    startBlackTimer()
                }
            }
            else -> {}
        }
    }
    
    private fun showResetDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.reset_dialog_title)
            .setMessage(R.string.reset_dialog_message)
            .setPositiveButton(R.string.reset_dialog_confirm) { _, _ ->
                finish()
            }
            .setNegativeButton(R.string.reset_dialog_cancel, null)
            .show()
    }
    
    private fun showExitDialog() {
        AlertDialog.Builder(this)
            .setTitle("Exit Game?")
            .setMessage("Are you sure you want to exit? The game will be lost.")
            .setPositiveButton("Exit") { _, _ ->
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun gameOver(winner: ActivePlayer) {
        gameState = GameState.FINISHED
        whiteTimer?.cancel()
        blackTimer?.cancel()
        
        soundManager.playGameOverSound()
        soundManager.vibrateGameOver()
        
        val winnerText = if (winner == ActivePlayer.WHITE) {
            getString(R.string.white_wins)
        } else {
            getString(R.string.black_wins)
        }
        
        AlertDialog.Builder(this)
            .setTitle(winnerText)
            .setMessage("Time's up!")
            .setPositiveButton("New Game") { _, _ ->
                finish()
            }
            .setNeutralButton("Exit") { _, _ ->
                finishAffinity()
            }
            .setCancelable(false)
            .show()
    }
    
    private fun updateUI() {
        updateWhiteTime()
        updateBlackTime()
        updatePlayerStates()
    }
    
    private fun updateWhiteTime() {
        val minutes = (whiteTimeMillis / 60000).toInt()
        val seconds = ((whiteTimeMillis % 60000) / 1000).toInt()
        binding.whitePlayerTime.text = String.format("%02d:%02d", minutes, seconds)
        
        val textColor = when {
            whiteTimeMillis <= CRITICAL_THRESHOLD_MS -> ContextCompat.getColor(this, R.color.error)
            whiteTimeMillis <= WARNING_THRESHOLD_MS -> ContextCompat.getColor(this, R.color.warning)
            else -> ContextCompat.getColor(this, R.color.white_player_text)
        }
        binding.whitePlayerTime.setTextColor(textColor)
    }
    
    private fun updateBlackTime() {
        val minutes = (blackTimeMillis / 60000).toInt()
        val seconds = ((blackTimeMillis % 60000) / 1000).toInt()
        binding.blackPlayerTime.text = String.format("%02d:%02d", minutes, seconds)
        
        val textColor = when {
            blackTimeMillis <= CRITICAL_THRESHOLD_MS -> ContextCompat.getColor(this, R.color.error)
            blackTimeMillis <= WARNING_THRESHOLD_MS -> ContextCompat.getColor(this, R.color.warning)
            else -> ContextCompat.getColor(this, R.color.black_player_text)
        }
        binding.blackPlayerTime.setTextColor(textColor)
    }
    
    private fun updatePlayerStates() {
        when (activePlayer) {
            ActivePlayer.WHITE -> {
                binding.whitePlayerArea.alpha = 1.0f
                binding.blackPlayerArea.alpha = 0.5f
                binding.whiteTapHint.alpha = 1.0f
                binding.blackTapHint.alpha = 0f
            }
            ActivePlayer.BLACK -> {
                binding.whitePlayerArea.alpha = 0.5f
                binding.blackPlayerArea.alpha = 1.0f
                binding.whiteTapHint.alpha = 0f
                binding.blackTapHint.alpha = 1.0f
            }
        }
    }
    
    private fun animatePlayerSwitch() {
        val activeArea = if (activePlayer == ActivePlayer.WHITE) {
            binding.whitePlayerArea
        } else {
            binding.blackPlayerArea
        }
        
        // Quick flash animation
        ObjectAnimator.ofFloat(activeArea, "alpha", 0.5f, 1.0f, 0.5f, 1.0f).apply {
            duration = 200
            start()
        }
    }
    
    private fun checkWhiteWarnings() {
        if (whiteTimeMillis <= CRITICAL_THRESHOLD_MS && !whiteWarningPlayed) {
            soundManager.playCriticalSound()
            whiteWarningPlayed = true
        } else if (whiteTimeMillis <= WARNING_THRESHOLD_MS && !whiteWarningPlayed) {
            soundManager.playWarningSound()
            soundManager.vibrateWarning()
            whiteWarningPlayed = true
        }
    }
    
    private fun checkBlackWarnings() {
        if (blackTimeMillis <= CRITICAL_THRESHOLD_MS && !blackWarningPlayed) {
            soundManager.playCriticalSound()
            blackWarningPlayed = true
        } else if (blackTimeMillis <= WARNING_THRESHOLD_MS && !blackWarningPlayed) {
            soundManager.playWarningSound()
            soundManager.vibrateWarning()
            blackWarningPlayed = true
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        whiteTimer?.cancel()
        blackTimer?.cancel()
        soundManager.release()
        wakeLock?.release()
    }
    
    override fun onBackPressed() {
        showExitDialog()
    }
}
