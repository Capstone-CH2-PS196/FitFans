package com.capstonech2.fitfans.ui.auth.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.databinding.ActivityLoginBinding
import com.capstonech2.fitfans.ui.MainActivity
import com.capstonech2.fitfans.ui.auth.basicinformation.BasicInformationActivity
import com.capstonech2.fitfans.ui.auth.register.RegisterActivity
import com.capstonech2.fitfans.utils.MessageUtils
import com.capstonech2.fitfans.utils.State
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
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    private val viewModel: LoginViewModel by viewModel()
    private val context = this@LoginActivity

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
            startActivity(Intent(context, RegisterActivity::class.java))
        }
    }

    private fun setFirebaseConfig(){
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)
        auth = Firebase.auth
    }

    private fun onClickLogin(){
        binding.buttonLogin.setOnClickListener {
            showLoading(true)
            binding.apply {
                val email = loginEdEmail.text.toString()
                val password = loginEdPassword.text.toString()

                val emailError = if (email.isEmpty()) "Email cannot be empty" else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) "Invalid email format" else null
                val passwordError = if (password.isEmpty()) "Password cannot be empty" else if (password.length < 8) "Password must be at least 8 characters" else null

                loginEmailLayout.error = emailError
                loginPasswordLayout.error = passwordError

                if (emailError == null && passwordError == null) {
                    signInWithEmailAndPassword(email, password)
                } else {
                    showLoading(false)
                }
            }
        }
    }

    private fun onClickLoginWithGoogle(){
        binding.buttonLoginWithGoogle.setOnClickListener {
            resultLauncher.launch(googleSignInClient.signInIntent)
        }
    }

    private fun signInWithEmailAndPassword(email: String, password: String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    if(auth.currentUser != null && auth.currentUser!!.isEmailVerified){
                        MessageUtils.showToast(context, "Login Success")
                        showLoading(false)
                        checkUserData()
                        finish()
                    } else {
                        showLoading(false)
                        MessageUtils.showDialog(context, "Please Verify your Email First")
                    }
                }
            }
            .addOnFailureListener {
                showLoading(false)
                MessageUtils.showDialog(context, "Invalid email or password. Please check and try again.")
            }
    }

    private fun authWithGoogle(idToken: String) {
        showLoading(true)
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                showLoading(false)
                MessageUtils.showToast(context, "Login with Google Success")
                finish()
            } else {
                showLoading(false)
                MessageUtils.showToast(context, "Login with Google Failed")
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
                checkUserData()
            } catch (e: ApiException) {
                MessageUtils.showDialog(context, "Google sign in failed")
            }
        }
    }

    private fun checkUserData(){
        val email = FirebaseAuth.getInstance().currentUser?.email
        viewModel.checkUserData(email.toString()).observe(context){ state ->
            if(state != null){
                when(state){
                    is State.Loading -> {}
                    is State.Success -> {
                        val intent = if (state.data.isNotEmpty()){
                            Intent(this, MainActivity::class.java)
                        } else {
                            Intent(this, BasicInformationActivity::class.java)
                        }
                        startActivity(intent)
                    }
                    is State.Error -> {}
                }
            }
        }
    }

    private fun showLoading(state: Boolean){
        binding.progressBarLogin.isVisible = state
    }
}