package com.capstonech2.fitfans.ui.auth.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstonech2.fitfans.databinding.ActivityLoginBinding
import com.capstonech2.fitfans.ui.auth.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBack()
        navigateToRegister()
    }

    private fun onBack(){
        binding.loginButtonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun navigateToRegister(){
        binding.registerNavigation.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}