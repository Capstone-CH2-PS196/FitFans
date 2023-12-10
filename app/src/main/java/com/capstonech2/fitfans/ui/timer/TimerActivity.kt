package com.capstonech2.fitfans.ui.timer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.databinding.ActivityTimerBinding
import com.capstonech2.fitfans.utils.TimerUtil

class TimerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimerBinding

    enum class TimerState {
        Stopped, Paused, Running
    }

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds: Long = 0
    private var timerState = TimerState.Stopped

    private var secondsRemaining: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.timer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btStart.setOnClickListener {
            startTimer()
            timerState = TimerState.Running
            updateButtons()
        }

        binding.btPause.setOnClickListener {
            timer.cancel()
            timerState = TimerState.Paused
            updateButtons()
        }

        binding.btReset.setOnClickListener {
            timer.cancel()
            onTimerFinished()
        }

        binding.btOk.setOnClickListener{

        }
    }

    override fun onResume() {
        super.onResume()
        initTimer()
    }

    override fun onPause() {
        super.onPause()

        if (timerState == TimerState.Running) {
            timer.cancel()
            } else if (timerState == TimerState.Paused) {
        }

        TimerUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, this)
        TimerUtil.setSecondsRemaining(secondsRemaining, this)
        TimerUtil.setTimerState(timerState, this)
    }

    private fun initTimer() {
        timerState = TimerUtil.getTimerState(this)

        if (timerState == TimerState.Stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

        secondsRemaining = if (timerState == TimerState.Running || timerState == TimerState.Paused)
            TimerUtil.getSecondsRemaining(this)
        else
            timerLengthSeconds

        if (timerState == TimerState.Running)
            startTimer()

        updateButtons()
        updateCountdownUI()
    }

    private fun onTimerFinished() {
        timerState = TimerState.Stopped
        setNewTimerLength()
        TimerUtil.setSecondsRemaining(timerLengthSeconds, this)
        secondsRemaining = timerLengthSeconds

        updateButtons()
        updateCountdownUI()
    }

    private fun startTimer() {
        timerState = TimerState.Running
        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()
            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun setNewTimerLength() {
        val lengthInMinutes = TimerUtil.getTimerLength(this)
        timerLengthSeconds = (lengthInMinutes * 60L)
    }

    private fun setPreviousTimerLength() {
        timerLengthSeconds = TimerUtil.getPreviousTimerLengthSeconds(this)
    }

    private fun updateCountdownUI() {
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        binding.tvTimer.text = "$minutesUntilFinished    :  ${
            if (secondsStr.length == 2) secondsStr else "0" + secondsStr
        }"
    }

    private fun updateButtons() {
        when (timerState) {
            TimerState.Running -> {
                binding.btStart.isEnabled = false
                binding.btPause.isEnabled = true
                binding.btReset.isEnabled = true
            }

            TimerState.Stopped -> {
                binding.btStart.isEnabled = true
                binding.btPause.isEnabled = false
                binding.btReset.isEnabled = false
            }

            TimerState.Paused -> {
                binding.btStart.isEnabled = true
                binding.btPause.isEnabled = false
                binding.btReset.isEnabled = true
            }
        }
    }

    private fun updateAlert() {
        }
    
}



