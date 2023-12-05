package com.capstonech2.fitfans.ui.camera

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.capstonech2.fitfans.databinding.ActivityCameraBinding
import com.capstonech2.fitfans.ui.detectionresult.DetectionResultActivity
import com.capstonech2.fitfans.utils.getImageUri

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = "Take Photo"
            setDisplayHomeAsUpEnabled(true)
        }

        binding.buttonCamera.setOnClickListener { takePhoto() }
        binding.buttonDetect.setOnClickListener { detectPhoto() }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            binding.previewImage.setImageURI(currentImageUri)
            binding.previewImageText.visibility = View.GONE
        }
    }

    private fun takePhoto(){
        currentImageUri = getImageUri(this)
        launcherCamera.launch(currentImageUri)
    }

    private fun detectPhoto(){
        startActivity(Intent(this, DetectionResultActivity::class.java))
    }
}