package com.capstonech2.fitfans.ui.timer

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class TimerViewModel : ViewModel() {

    private var timer: CountDownTimer? = null

    private val initialTime = MutableLiveData<Long>()
    private val currentTime = MutableLiveData<Long>()

    val currentTimeString = currentTime.map { time ->
        DateUtils.formatElapsedTime(time / 1000)
    }

    private val _eventCountDownFinish = MutableLiveData<Boolean>()
    val eventCountDownFinish: LiveData<Boolean> = _eventCountDownFinish

    fun setInitialTime(minuteFocus: Long) {
        val initialTimeMillis = minuteFocus * 60 * 1000
        initialTime.value = initialTimeMillis
        currentTime.value = initialTimeMillis

        timer = object : CountDownTimer(initialTimeMillis, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                currentTime.value = millisUntilFinished
            }

            override fun onFinish() {
                resetTimer()
            }
        }
    }

    private val _eventCountDownPause = MutableLiveData<Boolean>()
    val eventCountDownPause: LiveData<Boolean> = _eventCountDownPause

    private var isPaused = false

    fun pauseTimer() {
        timer?.cancel()
        isPaused = true
        _eventCountDownPause.value = true
    }

    fun startTimer() {
        timer?.start()
    }

    fun resetTimer() {
        timer?.cancel()
        currentTime.value = initialTime.value
        _eventCountDownFinish.value = true
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    private fun updateAlert() {
    }
}