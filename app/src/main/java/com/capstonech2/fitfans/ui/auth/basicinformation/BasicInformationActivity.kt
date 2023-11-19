package com.capstonech2.fitfans.ui.auth.basicinformation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstonech2.fitfans.databinding.ActivityBasicInformationBinding
import com.capstonech2.fitfans.ui.MainActivity

class BasicInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBasicInformationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasicInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        onBack()
        onSaveInfoClick()
    }

    private fun onBack(){
        binding.infoButtonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun onSaveInfoClick(){
        binding.infoButtonSave.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}