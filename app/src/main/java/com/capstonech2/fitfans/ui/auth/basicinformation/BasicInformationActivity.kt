package com.capstonech2.fitfans.ui.auth.basicinformation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import com.capstonech2.fitfans.data.model.User
import com.capstonech2.fitfans.databinding.ActivityBasicInformationBinding
import com.capstonech2.fitfans.ui.MainActivity
import com.capstonech2.fitfans.utils.State
import com.capstonech2.fitfans.utils.show
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

    private fun onBack(){
        binding.infoButtonBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun getSelectedRadioButton(){
        binding.infoRbgGender.setOnCheckedChangeListener { _, checked ->
            gender = findViewById<RadioButton>(checked).text.toString()
        }
    }

    private fun onSaveInfoClick(){
        binding.infoButtonSave.setOnClickListener {
            binding.progressBarInfo.show(true)
            val email = auth.currentUser?.email
            val name = binding.infoEdName.text.toString()
            val age = binding.infoEdAge.text.toString()
            val weight = binding.infoEdWeight.text.toString()
            val height = binding.infoEdHeight.text.toString()
            if (email != null) {
                val data = User(
                    full_name = name,
                    gender = gender,
                    age = age.toInt(),
                    weight = weight.toDouble(),
                    height = height.toDouble(),
                    email = email.toString()
                )
                viewModel.insertUser(data).observe(this){
                    when(it){
                        is State.Loading -> binding.progressBarInfo.show(true)
                        is State.Success -> {
                            binding.progressBarInfo.show(false)
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                        is State.Error -> {
                            binding.progressBarInfo.show(false)
                            // TODO
                        }
                    }
                }
            }
        }
    }
}