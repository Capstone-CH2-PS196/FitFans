package com.capstonech2.fitfans.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstonech2.fitfans.databinding.ActivityRegisterBinding
import com.capstonech2.fitfans.ui.auth.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBack()
        navigateToLogin()
    }

    private fun onBack(){
        binding.registerButtonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun navigateToLogin(){
        binding.loginNavigation.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}