package com.capstonech2.fitfans.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.databinding.ActivityRegisterBinding
import com.capstonech2.fitfans.ui.auth.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
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
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun register(){
        showLoading(true)
        binding.apply {
            val email = registerEdEmail.text.toString()
            val password = registerEdPassword.text.toString()

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        showLoading(false)
                        if (auth.currentUser != null){
                            auth.currentUser!!.sendEmailVerification()
                        }
                        showToast(getString(R.string.register_success_message))
                        auth.signOut()
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    }
                }
                .addOnFailureListener { exception ->
                    exception.localizedMessage?.let {
                        showToast(it)
                    }
                }
        }
    }

    private fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showLoading(state: Boolean){
        binding.progressBarRegister.isVisible = state
    }
}