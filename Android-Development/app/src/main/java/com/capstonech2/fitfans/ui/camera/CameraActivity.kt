package com.capstonech2.fitfans.ui.camera

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.data.model.Predicts
import com.capstonech2.fitfans.data.model.TimerRecommendation
import com.capstonech2.fitfans.databinding.ActivityCameraBinding
import com.capstonech2.fitfans.ui.detectionresult.DetectionResultActivity
import com.capstonech2.fitfans.utils.EXTRA_DETECT_RESULT
import com.capstonech2.fitfans.utils.State
import com.capstonech2.fitfans.utils.getImageUri
import com.capstonech2.fitfans.utils.reduceFileImage
import com.capstonech2.fitfans.utils.show
import com.capstonech2.fitfans.utils.showDialog
import com.capstonech2.fitfans.utils.uriToFile
import org.koin.androidx.viewmodel.ext.android.viewModel

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private val viewModel: CameraViewModel by viewModel()
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
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
        startLoadingState()
        if(currentImageUri != null){
            val imageFile = uriToFile(currentImageUri!!, this).reduceFileImage()
            viewModel.uploadImage(imageFile).observe(this){ result ->
                if (result != null){
                    when(result){
                        is State.Loading -> startLoadingState()
                        is State.Success -> {
                            val recommendation = TimerRecommendation(
                                expert = result.data.timerRecomendation.expert.toLong(),
                                ideal = result.data.timerRecomendation.ideal.toLong(),
                                beginner = result.data.timerRecomendation.beginner.toLong()
                            )
                            val resultPredict = Predicts(
                                image = currentImageUri.toString(),
                                timerRecommendation = recommendation,
                                toolName = result.data.toolName,
                                howToUse = result.data.howToUse,
                                toolDescription = result.data.toolDescription
                            )
                            detectionResult(resultPredict)
                            finishLoadingState()
                        }
                        is State.Error -> {
                            showDialog(this, getString(R.string.failed_detect_image))
                            finishLoadingState()
                        }
                    }
                }
            }
        } else {
            finishLoadingState()
            showDialog(this, getString(R.string.empty_image))
        }
//        currentImageUri?.let { uri ->
//
//        } ?: {
//
//        }
    }

    private fun detectionResult(data: Predicts){
        val intent = Intent(this, DetectionResultActivity::class.java)
        intent.putExtra(EXTRA_DETECT_RESULT, data)
        startActivity(intent)
    }

    private fun startLoadingState(){
        binding.apply {
            progressBarCamera.show(true)
            buttonCamera.isEnabled = false
            buttonDetect.isEnabled = false
        }
    }

    private fun finishLoadingState(){
        binding.apply {
            progressBarCamera.show(false)
            buttonCamera.isEnabled = true
            buttonDetect.isEnabled = true
        }
    }
}