package com.capstonech2.fitfans.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
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
            showLoading(true)
            binding.apply {
                val email = registerEdEmail.text.toString()
                val password = registerEdPassword.text.toString()

                val emailError = if (email.isEmpty()) getString(R.string.email_empty)
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) getString(R.string.invalid_email)
                    else null

                val passwordError = if (password.isEmpty()) getString(R.string.password_empty)
                else if (password.length < 8) getString(R.string.password_format)
                    else null

                registerEmailLayout.error = emailError
                registerPasswordLayout.error = passwordError

                if (emailError == null && passwordError == null) {
                    createUserWithEmailAndPassword(email, password)
                } else {
                    showLoading(false)
                }
            }
        }
    }

    private fun navigateToLogin(){
        binding.loginNavigation.setOnClickListener {
            startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    private fun createUserWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    if (auth.currentUser != null){
                        auth.currentUser!!.sendEmailVerification()
                    }
                    MessageUtils.showToast(context, getString(R.string.register_success_message))
                    showLoading(false)
                    auth.signOut()
                    startActivity(Intent(context, LoginActivity::class.java))
                }
            }
            .addOnFailureListener {
                MessageUtils.showDialog(context, getString(R.string.login_check))
                showLoading(false)
            }
    }

    private fun showLoading(state: Boolean){
        binding.progressBarRegister.isVisible = state
    }
}