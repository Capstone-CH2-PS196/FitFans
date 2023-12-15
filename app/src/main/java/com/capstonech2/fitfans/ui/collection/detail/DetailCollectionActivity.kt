package com.capstonech2.fitfans.ui.collection.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.data.model.Recommendation
import com.capstonech2.fitfans.databinding.ActivityDetailCollectionBinding
import com.capstonech2.fitfans.ui.collection.CollectionAdapter.Companion.EXTRA_COLLECTION
import com.capstonech2.fitfans.ui.collection.CollectionViewModel
import com.capstonech2.fitfans.utils.loadImage
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailCollectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailCollectionBinding
    private val viewModel: CollectionViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCollectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpTopBar()
        setDataFromIntent()
    }

    private fun setUpTopBar() {
        supportActionBar?.apply {
            title = "Detail Collection"
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setDataFromIntent(){
        val dataId = intent.getIntExtra(EXTRA_COLLECTION, 0)
        Log.i("test", dataId.toString())
        viewModel.getCollectionById(dataId).observe(this){ result ->
            binding.apply {
                collectImage.loadImage(result.image)
                collectName.text = result.toolName
                collectDescription.text = result.toolDescription
                showGuidanceList(result.howToUse)
                val recommendation = listOf(
                    Recommendation(
                        level = "Beginner : ",
                        time = "${result.timerRecommendation.beginner} minutes"
                    ),
                    Recommendation(
                        level = "Ideal    : ",
                        time = "${result.timerRecommendation.ideal} minutes"
                    ),
                    Recommendation(
                        level = "Expert   : ",
                        time = "${result.timerRecommendation.expert} minutes"
                    ),
                )
                showRecommendationList(recommendation)
            }
        }
    }

    private fun showRecommendationList(data :List<Recommendation>) {
        binding.recommendationList.layoutManager = LinearLayoutManager(this@DetailCollectionActivity)
        val adapter = CollectRecommendAdapter()
        adapter.submitList(data)
        binding.recommendationList.adapter = adapter
    }

    private fun showGuidanceList(data :List<String>) {
        binding.guidanceDescription.layoutManager = LinearLayoutManager(this@DetailCollectionActivity)
        val adapter = CollectGuidanceAdapter()
        adapter.submitList(data)
        binding.guidanceDescription.adapter = adapter
    }
}