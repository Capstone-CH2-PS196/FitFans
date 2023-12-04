package com.capstonech2.fitfans.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.data.model.User
import com.capstonech2.fitfans.databinding.ActivityEditProfileBinding
import com.capstonech2.fitfans.ui.MainActivity
import com.capstonech2.fitfans.utils.EXTRA_PROFILE_KEY
import com.capstonech2.fitfans.utils.State
import com.capstonech2.fitfans.utils.show
import com.capstonech2.fitfans.utils.showDialogWithAction
import com.capstonech2.fitfans.utils.showToast
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var auth: FirebaseAuth
    private val viewModel: ProfileViewModel by viewModel()
    private var currentImageUri: Uri? = null
    private var gender: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)

        getSelectedRadioButton()
        getDataFromIntent()
        setUpTopBar()
        saveChanges()
        changeImageProfile()
    }

    private fun setUpTopBar(){
        supportActionBar?.apply {
            title = getString(R.string.edit_profile)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun getDataFromIntent(){
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            intent.getParcelableExtra(EXTRA_PROFILE_KEY, User::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_PROFILE_KEY)
        }

        if (data != null){
            binding.apply {
                profileEdName.setText(data.full_name)
                profileEdAge.setText(data.age.toString())
                profileEdWeight.setText(data.weight.toString())
                profileEdHeight.setText(data.height.toString())
                when(data.gender){
                    "Male" -> rbMale.isChecked = true
                    "Female" -> rbFemale.isChecked = true
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun changeImageProfile(){
        binding.buttonChangeImage.setOnClickListener {
            startGallery()
        }
    }

    private val launcherPhotoPicker = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            binding.previewEditImage.setImageURI(currentImageUri)
        } else {
            showToast(this, getString(R.string.no_image))
        }
    }

    private fun startGallery() {
        launcherPhotoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun getSelectedRadioButton(){
        binding.profileRbgGender.setOnCheckedChangeListener { _, checked ->
            gender = findViewById<RadioButton>(checked).text.toString()
        }
    }

    private fun saveChanges() {
        binding.apply {
            profileButtonSave.setOnClickListener {
                binding.progressBarEditProfile.show(true)
                currentImageUri?.let { uri ->
                    val email = auth.currentUser?.email.toString()
                    val image = uri.toString()
                    val name = profileEdName.text.toString()
                    val gender = gender
                    val age = profileEdAge.text.toString()
                    val weight = profileEdWeight.text.toString()
                    val height = profileEdHeight.text.toString()
                    val data = User(
                        full_name = name,
                        gender = gender,
                        age = age.toInt(),
                        weight = weight.toDouble(),
                        height = height.toDouble(),
                        email = email,
                        image = image
                    )
                    updateUser(email, data)
                }
            }
        }
    }

    private fun updateUser(email: String, data: User){
        viewModel.updateUserByEmail(email, data).observe(this@EditProfileActivity){ state ->
            if(state != null){
                when(state){
                    is State.Loading -> {
                        binding.progressBarEditProfile.show(true)
                    }
                    is State.Success -> {
                        binding.progressBarEditProfile.show(false)
                        showDialogWithAction(this, "Success", "Your Profile has been Updated"){
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                    }
                    is State.Error -> {
                        binding.progressBarEditProfile.show(false)
                    }
                }
            }
        }
    }
}