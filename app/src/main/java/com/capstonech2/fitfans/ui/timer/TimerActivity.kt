package com.capstonech2.fitfans.ui.timer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.IntentCompat
import androidx.lifecycle.ViewModelProvider
import com.capstonech2.fitfans.data.model.Predicts
import com.capstonech2.fitfans.databinding.ActivityTimerBinding

class TimerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Timer"

        val predict= IntentCompat.getParcelableExtra(intent, Predicts::class.java)

        if (predict != null) {

            val viewModel = ViewModelProvider(this).get(TimerViewModel::class.java)

            viewModel.setInitialTime(predict.timerRecommendation)
            viewModel.currentTimeString.observe(this) {
                binding.tvTimer.text = it
            }

            binding.btStart.setOnClickListener {
                updateButtonState(true)
                viewModel.startTimer()
            }

            binding.btReset.setOnClickListener {
                viewModel.resetTimer()
                updateButtonState(false)
            }

            binding.btPause.setOnClickListener {
                viewModel.pauseTimer()
                updateButtonState(false)
            }
        }
    }

    private fun updateButtonState(isRunning: Boolean) {
        binding.btStart.isEnabled = !isRunning
        binding.btReset.isEnabled = isRunning
    }
}



