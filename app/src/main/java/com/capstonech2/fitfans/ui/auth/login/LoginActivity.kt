package com.capstonech2.fitfans.ui.auth.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.capstonech2.fitfans.ui.MainActivity
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.databinding.ActivityLoginBinding
import com.capstonech2.fitfans.ui.auth.basicinformation.BasicInformationActivity
import com.capstonech2.fitfans.ui.auth.register.RegisterActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setFirebaseConfig()

        onBack()
        onClickLogin()
        onClickLoginWithGoogle()
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

    private fun navigateToMainMenu(){
        if (auth.currentUser != null && auth.currentUser!!.isEmailVerified){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            showToast("")
        }
    }

    private fun setFirebaseConfig(){
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth
    }

    private fun onClickLogin(){
        binding.buttonLogin.setOnClickListener {
            auth()
        }
    }

    private fun onClickLoginWithGoogle(){
        binding.buttonLoginWithGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            resultLauncher.launch(signInIntent)
        }
    }

    private fun auth(){
        showLoading(true)
        binding.apply {
            val email = loginEdEmail.text.toString()
            val password = loginEdPassword.text.toString()

            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        showLoading(false)
                        if(auth.currentUser != null && auth.currentUser!!.isEmailVerified){
                            startActivity(Intent(this@LoginActivity, BasicInformationActivity::class.java))
                            finish()
                        } else {
                            showToast("Please Verify your Email First")
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    showLoading(false)
                    exception.localizedMessage?.let {
                        showToast(it)
                    }
                }
        }
    }

    private fun authWithGoogle(idToken: String) {
        showLoading(true)
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                showLoading(false)
                navigateToMainMenu()
            } else {
                val errorMessage = task.exception?.message
                showToast(errorMessage.toString())
            }
        }
    }

    private var resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)!!
                authWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showLoading(state: Boolean){
        binding.progressBarLogin.isVisible = state
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}