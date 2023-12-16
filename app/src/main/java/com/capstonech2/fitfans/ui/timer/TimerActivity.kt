package com.capstonech2.fitfans.ui.timer

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat.getParcelableExtra
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.data.model.Predicts
import com.capstonech2.fitfans.data.model.Recommendation
import com.capstonech2.fitfans.databinding.ActivityTimerBinding
import com.capstonech2.fitfans.utils.EXTRA_PREDICT
import com.capstonech2.fitfans.utils.EXTRA_RECOMMENDATION
import org.koin.androidx.viewmodel.ext.android.viewModel

class TimerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimerBinding
    private val viewModel: TimerViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = "Timer"
            setDisplayHomeAsUpEnabled(true)
        }
        setData()
        updateDialogUI()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setData() {
        val predict = getParcelableExtra(intent, EXTRA_PREDICT, Predicts::class.java)
        val recommendation = getParcelableExtra(intent,EXTRA_RECOMMENDATION, Recommendation::class.java)

        if(predict != null && recommendation != null){
            binding.tvTitle.text = recommendation.level
            binding.tvRecommendation.text = "Recommendation : ${recommendation.set}"

            viewModel.setInitialTime(recommendation.time)
            viewModel.currentTimeString.observe(this){ currentTime ->
                binding.tvTimer.text = currentTime
            }

            binding.btStart.setOnClickListener {
                viewModel.startTimer()
                updateButtonState(true)
            }

            binding.btPause.setOnClickListener {
                viewModel.pauseTimer()
                updateButtonState(false)
            }

            binding.btReset.setOnClickListener {
                viewModel.resetTimer()
                updateButtonState(false)
            }
        }
    }

    private fun updateDialogUI(){
        binding.btOk.setOnClickListener {
            binding.btOk.visibility = View.GONE
            binding.cardView.setCardBackgroundColor(Color.GRAY)
        }
    }

    private fun updateButtonState(isRunning: Boolean) {
        binding.btStart.isEnabled = !isRunning
        binding.btPause.isEnabled = isRunning
        binding.btReset.isEnabled = isRunning
    }
}



