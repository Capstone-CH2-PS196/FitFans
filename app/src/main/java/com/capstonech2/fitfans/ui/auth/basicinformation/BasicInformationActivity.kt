package com.capstonech2.fitfans.ui.auth.basicinformation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import com.capstonech2.fitfans.data.remote.response.UsersResponseItem
import com.capstonech2.fitfans.databinding.ActivityBasicInformationBinding
import com.capstonech2.fitfans.ui.MainActivity
import com.capstonech2.fitfans.utils.State
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
            val rbChecked: RadioButton = findViewById(checked)
            gender = rbChecked.text.toString()
        }
    }

    private fun onSaveInfoClick(){
        binding.infoButtonSave.setOnClickListener {
            val email = auth.currentUser?.email
            val name = binding.infoEdName.text.toString()
            val age = binding.infoEdAge.text.toString()
            val weight = binding.infoEdWeight.text.toString()
            val height = binding.infoEdHeight.text.toString()
            if (email != null) {
                val data = UsersResponseItem(
                    fullName = name,
                    gender = gender,
                    age = age.toInt(),
                    weight = weight.toDouble(),
                    height = height.toDouble(),
                    email = email.toString()
                )
                viewModel.insertUser(data).observe(this){
                    when(it){
                        is State.Loading -> {}
                        is State.Success -> {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                        is State.Error -> {
                            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}