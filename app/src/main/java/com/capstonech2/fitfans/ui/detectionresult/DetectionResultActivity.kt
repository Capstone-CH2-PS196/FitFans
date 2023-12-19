package com.capstonech2.fitfans.ui.detectionresult

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.IntentCompat.getParcelableExtra
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonech2.fitfans.data.model.Recommendation
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.data.model.Collection
import com.capstonech2.fitfans.data.model.Predicts
import com.capstonech2.fitfans.data.model.TimerRecommendationEntity
import com.capstonech2.fitfans.databinding.ActivityDetectionResultBinding
import com.capstonech2.fitfans.ui.collection.CollectionActivity
import com.capstonech2.fitfans.ui.collection.CollectionViewModel
import com.capstonech2.fitfans.utils.EXTRA_DETECT_RESULT
import com.capstonech2.fitfans.utils.capitalizeFirstLetter
import com.capstonech2.fitfans.utils.dialogDeleteAction
import com.capstonech2.fitfans.utils.loadImage
import com.capstonech2.fitfans.utils.showDialog
import com.capstonech2.fitfans.utils.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetectionResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetectionResultBinding
    private val viewModel: CollectionViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetectionResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpTopBar()
        setDataFromIntent()
    }

    private fun setUpTopBar() {
        supportActionBar?.apply {
            title = getString(R.string.detection_result)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.result_menu, menu)

        val data = getParcelableExtra(intent, EXTRA_DETECT_RESULT, Predicts::class.java)
        val menuItem1 = menu?.findItem(R.id.menu_collection)
        val menuItem2 = menu?.findItem(R.id.action_delete_collection)

        if (data != null){
            viewModel.getCollectionByImage(data.image).observe(this){
                if (it != null){
                    menuItem1?.let { menu1 ->
                        menu1.isVisible = false
                        menuItem2?.let { menu2 ->
                            menu2.isVisible = true
                        }
                    }
                } else {
                    menuItem1?.let { menu1 ->
                        menu1.isVisible = true
                        menuItem2?.let { menu2 ->
                            menu2.isVisible = false
                        }
                    }
                }
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val data = getParcelableExtra(intent, EXTRA_DETECT_RESULT, Predicts::class.java)
        return when (item.itemId) {
            R.id.menu_collection -> {
                if (data != null){
                    collectData(data)
                    showToast(this, getString(R.string.success_save_result))
                } else {
                    showDialog(this, getString(R.string.failed_save_result))
                }
                true
            }
            R.id.action_delete_collection -> {
                if (data != null){
                    dialogDeleteAction(this, getString(R.string.delete_collection), getString(R.string.delete_collection_message)) {
                        viewModel.deleteCollection(data.image)
                        showToast(this, getString(R.string.success_delete_collection))
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setDataFromIntent() {
        binding.apply {
            val data = getParcelableExtra(intent, EXTRA_DETECT_RESULT, Predicts::class.java)

            if (data != null){
                resultImage.loadImage(data.image)
                resultName.text = data.toolName.capitalizeFirstLetter()
                resultDescription.text = data.toolDescription
                showGuidanceList(data.howToUse)
                val recommendation = listOf(
                    Recommendation(
                        level = "Beginner",
                        set = "2 set x 12 times",
                        time = data.timerRecommendation.beginner
                    ),
                    Recommendation(
                        level = "Ideal",
                        set = "3 set x 12 times",
                        time = data.timerRecommendation.ideal
                    ),
                    Recommendation(
                        level = "Expert",
                        set = "4 set x 12 times",
                        time = data.timerRecommendation.expert
                    ),
                )
                showRecommendationList(recommendation)
            } else {
                finish()
            }
        }
    }

    private fun showRecommendationList(data :List<Recommendation>) {
        val result = getParcelableExtra(intent, EXTRA_DETECT_RESULT, Predicts::class.java)
        binding.recommendationList.layoutManager = LinearLayoutManager(this@DetectionResultActivity)
        if (result != null) {
            val adapter = RecommendationAdapter(result)
            adapter.submitList(data)
            binding.recommendationList.adapter = adapter
        }
    }

    private fun showGuidanceList(data :List<String>) {
        binding.guidanceDescription.layoutManager = LinearLayoutManager(this@DetectionResultActivity)
        val adapter = GuidanceAdapter()
        adapter.submitList(data)
        binding.guidanceDescription.adapter = adapter
    }

    private fun collectData(data: Predicts) {
        val collectRecommendation = TimerRecommendationEntity(
            expert = data.timerRecommendation.expert,
            ideal = data.timerRecommendation.ideal,
            beginner = data.timerRecommendation.beginner
        )
        val collect = Collection(
            image = data.image,
            toolName = data.toolName,
            toolDescription = data.toolDescription,
            howToUse = data.howToUse,
            timerRecommendation = collectRecommendation
        )
        viewModel.insertResult(collect)
    }
}