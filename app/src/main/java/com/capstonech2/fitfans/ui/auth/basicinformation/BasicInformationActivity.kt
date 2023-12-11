package com.capstonech2.fitfans.ui.auth.basicinformation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.data.model.User
import com.capstonech2.fitfans.databinding.ActivityBasicInformationBinding
import com.capstonech2.fitfans.ui.MainActivity
import com.capstonech2.fitfans.utils.State
import com.capstonech2.fitfans.utils.show
import com.capstonech2.fitfans.utils.showDialog
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel

class BasicInformationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBasicInformationBinding
    private lateinit var auth: FirebaseAuth

    private val viewModel: BasicInformationViewModel by viewModel()
    private var gender: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasicInformationBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)
        supportActionBar?.hide()

        onBack()
        getSelectedRadioButton()
        onSaveInfoClick()
    }

    private fun onBack() {
        binding.infoButtonBack.setOnClickListener {
            auth.signOut()
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun getSelectedRadioButton() {
        binding.infoRbgGender.setOnCheckedChangeListener { _, checked ->
            gender = findViewById<RadioButton>(checked).text.toString()
        }
    }

    private fun onSaveInfoClick() {
        binding.apply {
            infoButtonSave.setOnClickListener {
                startLoadingState()

                val email = auth.currentUser?.email
                val name = infoEdName.text.toString()
                val age = infoEdAge.text.toString()
                val weight = infoEdWeight.text.toString()
                val height = infoEdHeight.text.toString()

                val nameError = if (name.isEmpty()) getString(R.string.name_empty) else null
                val ageError = if (age.isEmpty()) getString(R.string.age_empty) else null
                val weightError = if (weight.isEmpty()) getString(R.string.weight_empty) else null
                val heightError = if (height.isEmpty()) getString(R.string.height_empty) else null

                if (nameError == null && ageError == null && weightError == null && heightError == null) {
                    val data = User(
                        full_name = name,
                        gender = gender,
                        age = age.toInt(),
                        weight = weight.toDouble(),
                        height = height.toDouble(),
                        email = email.toString(),
                        image = ""
                    )
                    insertData(data)
                } else {
                    finishLoadingState()
                    infoEdNameLayout.error = nameError
                    infoEdAgeLayout.error = ageError
                    infoEdWeightLayout.error = weightError
                    infoEdHeightLayout.error = heightError
                }
            }
        }
    }

    private fun insertData(data: User) {
        viewModel.insertUser(data).observe(this){
            when(it){
                is State.Loading -> startLoadingState()
                is State.Success -> handleSuccessState()
                is State.Error -> handleErrorState()
            }
        }
    }

    private fun handleSuccessState() {
        finishLoadingState()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun handleErrorState() {
        finishLoadingState()
        showDialog(this, getString(R.string.save_user_success))
    }

    private fun startLoadingState() {
        binding.apply {
            progressBarInfo.show(true)
            infoEdName.isEnabled = false
            infoEdAge.isEnabled = false
            infoEdWeight.isEnabled = false
            infoEdHeight.isEnabled = false
        }
    }

    private fun finishLoadingState() {
        binding.apply {
            progressBarInfo.show(false)
            infoEdName.isEnabled = true
            infoEdAge.isEnabled = true
            infoEdWeight.isEnabled = true
            infoEdHeight.isEnabled = true
        }
    }
}