package com.capstonech2.fitfans.ui.timer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.databinding.ActivityTimerBinding

class TimerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.timer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}