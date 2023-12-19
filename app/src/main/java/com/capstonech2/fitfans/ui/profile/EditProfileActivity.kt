package com.capstonech2.fitfans.ui.profile

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.IntentCompat.getParcelableExtra
import androidx.core.net.toUri
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.data.model.User
import com.capstonech2.fitfans.databinding.ActivityEditProfileBinding
import com.capstonech2.fitfans.ui.MainActivity
import com.capstonech2.fitfans.utils.EXTRA_PROFILE_KEY
import com.capstonech2.fitfans.utils.State
import com.capstonech2.fitfans.utils.loadImage
import com.capstonech2.fitfans.utils.show
import com.capstonech2.fitfans.utils.showDialog
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
        val data = getParcelableExtra(intent, EXTRA_PROFILE_KEY, User::class.java)

        if (data != null){
            binding.apply {
                currentImageUri = data.image?.toUri()

                if (currentImageUri == null || currentImageUri.toString() == "")
                    previewEditImage.setImageResource(R.drawable.ic_profile_user)
                else previewEditImage.loadImage(currentImageUri.toString())

                profileEdName.setText(data.full_name)
                profileEdAge.setText(data.age.toString())
                profileEdWeight.setText(data.weight.toString())
                profileEdHeight.setText(data.height.toString())

                when(data.gender){
                    getString(R.string.male_label) -> rbMale.isChecked = true
                    getString(R.string.female_label) -> rbFemale.isChecked = true
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
            binding.previewEditImage.loadImage(currentImageUri.toString())
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

                    val nameError = if (name.isEmpty()) getString(R.string.name_empty) else null
                    val ageError = if (age.isEmpty()) getString(R.string.age_empty) else null
                    val weightError = if (weight.isEmpty()) getString(R.string.weight_empty) else null
                    val heightError = if (height.isEmpty()) getString(R.string.height_empty) else null

                    if (nameError == null && ageError == null && weightError == null &&
                        heightError == null){
                        val data = User(
                            full_name = name,
                            gender = gender,
                            age = age.toInt(),
                            weight = weight.toDouble(),
                            height = height.toDouble(),
                            email = email,
                            image = image,
                            total_calories = 0.0
                        )
                        updateUser(email, data)
                    } else {
                        progressBarEditProfile.show(false)
                        profileEdNameLayout.error = nameError
                        profileEdAgeLayout.error = ageError
                        profileEdWeightLayout.error = weightError
                        profileEdHeightLayout.error = heightError
                    }
                }
            }
        }
    }

    private fun updateUser(email: String, data: User) {
        viewModel.updateUserByEmail(email, data).observe(this){ state ->
            if(state != null){
                when(state){
                    is State.Loading -> startLoadingState()
                    is State.Success -> handleSuccessState()
                    is State.Error -> handleErrorState()
                }
            }
        }
    }

    private fun startLoadingState() {
        binding.apply {
            progressBarEditProfile.show(true)
            buttonChangeImage.isEnabled = false
            profileEdName.isEnabled = false
            profileEdAge.isEnabled = false
            profileEdWeight.isEnabled = false
            profileEdHeight.isEnabled = false
            profileButtonSave.isEnabled = false
        }
    }

    private fun finishLoadingState() {
        binding.apply {
            progressBarEditProfile.show(false)
            buttonChangeImage.isEnabled = true
            profileEdName.isEnabled = true
            profileEdAge.isEnabled = true
            profileEdWeight.isEnabled = true
            profileEdHeight.isEnabled = true
            profileButtonSave.isEnabled = true
        }
    }

    private fun handleSuccessState() {
        finishLoadingState()
        showDialogWithAction(this, getString(R.string.success), getString(R.string.update_profile_success)){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun handleErrorState() {
        finishLoadingState()
        showDialog(this, getString(R.string.save_user_success))
    }
}