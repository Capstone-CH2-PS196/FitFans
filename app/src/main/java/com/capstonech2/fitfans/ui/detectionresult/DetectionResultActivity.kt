package com.capstonech2.fitfans.ui.detectionresult

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.databinding.ActivityDetectionResultBinding
import com.capstonech2.fitfans.model.Recommendation

class DetectionResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetectionResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectionResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpTopBar()
        showRecommendationList(FakeRecommendation.recommendation)
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

    private fun showRecommendationList(data :List<Recommendation>) {
        binding.recommendationList.layoutManager = LinearLayoutManager(this@DetectionResultActivity)
        val adapter = DetectionResultAdapter()
        adapter.submitList(data)
        binding.recommendationList.adapter = adapter
    }
}