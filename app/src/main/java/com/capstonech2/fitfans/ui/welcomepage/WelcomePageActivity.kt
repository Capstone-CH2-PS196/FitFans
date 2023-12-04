package com.capstonech2.fitfans.ui.welcomepage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.capstonech2.fitfans.databinding.ActivityWelcomePageBinding
import com.capstonech2.fitfans.ui.MainActivity
import com.capstonech2.fitfans.ui.auth.login.LoginActivity
import com.capstonech2.fitfans.ui.auth.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth

class WelcomePageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomePageBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomePageBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)

        navigateToLogin()
        navigateToRegister()
        checkUserStatus()
    }

    private fun checkUserStatus(){
        if(auth.currentUser?.email != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun navigateToLogin(){
        binding.welcomeButtonLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun navigateToRegister(){
        binding.welcomeButtonRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}