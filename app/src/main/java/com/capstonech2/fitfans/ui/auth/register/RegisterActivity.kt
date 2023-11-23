package com.capstonech2.fitfans.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.databinding.ActivityRegisterBinding
import com.capstonech2.fitfans.ui.auth.login.LoginActivity
import com.capstonech2.fitfans.utils.MessageUtils
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val context = this@RegisterActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()

        onBack()
        onClickRegister()
        navigateToLogin()
    }

    private fun onBack(){
        binding.registerButtonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun onClickRegister(){
        binding.buttonRegister.setOnClickListener {
            register()
        }
    }

    private fun navigateToLogin(){
        binding.loginNavigation.setOnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    private fun register(){
        showLoading(true)
        binding.apply {
            val email = registerEdEmail.text.toString()
            val password = registerEdPassword.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            showLoading(false)
                            if (auth.currentUser != null){
                                auth.currentUser!!.sendEmailVerification()
                            }
                            MessageUtils.showToast(context, getString(R.string.register_success_message))
                            auth.signOut()
                            startActivity(Intent(context, LoginActivity::class.java))
                        }
                    }
                    .addOnFailureListener {
                        showLoading(false)
                        MessageUtils.showDialog(context, "Register Failed")
                    }
            } else {
                showLoading(false)
                MessageUtils.showDialog(context, "Invalid email or password. Please check and try again.")
            }
        }
    }

    private fun showLoading(state: Boolean){
        binding.progressBarRegister.isVisible = state
    }
}