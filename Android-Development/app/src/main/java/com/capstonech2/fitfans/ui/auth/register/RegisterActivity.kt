package com.capstonech2.fitfans.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.databinding.ActivityRegisterBinding
import com.capstonech2.fitfans.ui.auth.login.LoginActivity
import com.capstonech2.fitfans.utils.show
import com.capstonech2.fitfans.utils.showToast
import com.capstonech2.fitfans.utils.showDialog
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
        binding.apply {
            buttonRegister.setOnClickListener {
                startLoadingState()

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

                if (emailError == null && passwordError == null) createUserWithEmailAndPassword(
                    email,
                    password
                )
                else finishLoadingState()
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
                    finishLoadingState()
                    auth.signOut()
                    startActivity(Intent(context, LoginActivity::class.java))
                    showToast(context, getString(R.string.register_success_message))
                }
            }
            .addOnFailureListener {
                finishLoadingState()
                showDialog(context, getString(R.string.auth_check))
            }
    }

    private fun startLoadingState() {
        binding.apply {
            progressBarRegister.show(true)
            registerEdEmail.isEnabled = false
            registerEdPassword.isEnabled = false
            buttonRegister.isEnabled = false
        }
    }

    private fun finishLoadingState() {
        binding.apply {
            progressBarRegister.show(false)
            registerEdEmail.isEnabled = true
            registerEdPassword.isEnabled = true
            buttonRegister.isEnabled = true
        }
    }
}