package com.capstonech2.fitfans.ui.detectionresult

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonech2.fitfans.data.model.Recommendation
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.data.model.Predicts
import com.capstonech2.fitfans.databinding.ActivityDetectionResultBinding
import com.capstonech2.fitfans.ui.camera.CameraActivity.Companion.EXTRA_DETECT_RESULT
import com.capstonech2.fitfans.utils.loadImage

class DetectionResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetectionResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectionResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpTopBar()
        setDataFromIntent()
    }

    private fun setUpTopBar(){
        supportActionBar?.apply {
            title = getString(R.string.detection_result)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setDataFromIntent(){
        binding.apply {
            val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                intent.getParcelableExtra(EXTRA_DETECT_RESULT, Predicts::class.java)
            } else {
                intent.getParcelableExtra(EXTRA_DETECT_RESULT)
            }

            if (data != null){
                resultImage.loadImage(data.image)
                resultName.text = data.toolName
                resultDescription.text = data.toolDescription
                showGuidanceList(data.howToUse)

                val recommendation = listOf(
                    Recommendation(
                        level = "Beginner : ",
                        time = "${data.timerRecommendation.beginner} minutes"
                    ),
                    Recommendation(
                        level = "Ideal    : ",
                        time = "${data.timerRecommendation.ideal} minutes"
                    ),
                    Recommendation(
                        level = "Expert   : ",
                        time = "${data.timerRecommendation.expert} minutes"
                    ),
                )
                showRecommendationList(recommendation)
            }
        }
    }

    private fun showRecommendationList(data :List<Recommendation>) {
        binding.recommendationList.layoutManager = LinearLayoutManager(this@DetectionResultActivity)
        val adapter = RecommendationAdapter()
        adapter.submitList(data)
        binding.recommendationList.adapter = adapter
    }

    private fun showGuidanceList(data :List<String>) {
        binding.guidanceDescription.layoutManager = LinearLayoutManager(this@DetectionResultActivity)
        val adapter = GuidanceAdapter()
        adapter.submitList(data)
        binding.guidanceDescription.adapter = adapter
    }
}