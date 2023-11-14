package com.capstonech2.fitfans.ui.auth.basicinformation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstonech2.fitfans.databinding.ActivityBasicInformationBinding

class BasicInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBasicInformationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasicInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBack()
    }

    private fun onBack(){
        binding.infoButtonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}