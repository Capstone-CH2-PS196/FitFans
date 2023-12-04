package com.capstonech2.fitfans.ui.auth.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.data.remote.response.UsersResponseItem
import com.capstonech2.fitfans.databinding.ActivityLoginBinding
import com.capstonech2.fitfans.ui.MainActivity
import com.capstonech2.fitfans.ui.auth.basicinformation.BasicInformationActivity
import com.capstonech2.fitfans.ui.auth.register.RegisterActivity
import com.capstonech2.fitfans.utils.State
import com.capstonech2.fitfans.utils.show
import com.capstonech2.fitfans.utils.showToast
import com.capstonech2.fitfans.utils.showDialog
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
        binding.apply {
            buttonLogin.setOnClickListener {
                progressBarLogin.show(true)

                val email = loginEdEmail.text.toString()
                val password = loginEdPassword.text.toString()

                val emailError = if (email.isEmpty()) getString(R.string.email_empty)
                    else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) getString(R.string.invalid_email)
                    else null

                val passwordError = if (password.isEmpty()) getString(R.string.password_empty)
                    else if (password.length < 8) getString(R.string.password_format)
                    else null

                loginEmailLayout.error = emailError
                loginPasswordLayout.error = passwordError

                if (emailError == null && passwordError == null) signInWithEmailAndPassword(
                    email,
                    password
                )
                else progressBarLogin.show(false)
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
                        showToast(context, getString(R.string.login_success_message))
                        binding.progressBarLogin.show(false)
                        checkUserData()
                        finish()
                    } else {
                        binding.progressBarLogin.show(false)
                        showDialog(context, getString(R.string.email_verify_null))
                    }
                }
            }
            .addOnFailureListener {
                binding.progressBarLogin.show(false)
                showDialog(context, getString(R.string.auth_check))
            }
    }

    private fun authWithGoogle(idToken: String) {
        binding.progressBarLogin.show(true)
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                binding.progressBarLogin.show(false)
                showToast(context, getString(R.string.login_google_success_message))
                finish()
            } else {
                binding.progressBarLogin.show(false)
                showToast(context, getString(R.string.login_google_failed_message))
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
                showDialog(context, getString(R.string.login_google_failed_message))
            }
        }
    }

    private fun checkUserData(){
        viewModel.checkUserData(auth.currentUser?.email.toString())
        viewModel.userData.observe(this){ state ->
            when(state){
                is State.Loading -> handleLoadingState()
                is State.Success -> {
                    handleSuccessState(state.data)
                }
                is State.Error -> handleErrorState(state.error)
            }
        }
    }

    private fun handleLoadingState() {
        binding.progressBarLogin.show(true)
    }

    private fun handleSuccessState(data: List<UsersResponseItem>) {
        binding.progressBarLogin.show(false)
        val intent = if (data.isNotEmpty()) {
            Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
        } else {
            Intent(this, BasicInformationActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
        }
        startActivity(intent)
    }

    private fun handleErrorState(errorMessage: String) {
        binding.progressBarLogin.show(false)
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
    }
}