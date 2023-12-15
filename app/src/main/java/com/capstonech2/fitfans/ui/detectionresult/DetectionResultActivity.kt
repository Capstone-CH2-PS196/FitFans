package com.capstonech2.fitfans.ui.detectionresult

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonech2.fitfans.data.model.Recommendation
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.data.model.Collection
import com.capstonech2.fitfans.data.model.Predicts
import com.capstonech2.fitfans.data.model.TimerRecommendationEntity
import com.capstonech2.fitfans.databinding.ActivityDetectionResultBinding
import com.capstonech2.fitfans.ui.camera.CameraActivity.Companion.EXTRA_DETECT_RESULT
import com.capstonech2.fitfans.ui.collection.CollectionViewModel
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
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            intent.getParcelableExtra(EXTRA_DETECT_RESULT, Predicts::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_DETECT_RESULT)
        }
        val menuItem1 = menu?.findItem(R.id.menu_collection)
        val menuItem2 = menu?.findItem(R.id.action_delete_collection)
        if (data != null){
            viewModel.getCollectionByImage(data.image).observe(this){
                if (it != null){
                    menuItem1?.let { menu1 ->
                        menu1.isEnabled = false
                        menuItem2?.let { menu2 ->
                            menu2.isEnabled = true
                        }
                    }
                } else {
                    menuItem1?.let { menu1 ->
                        menu1.isEnabled = true
                        menuItem2?.let { menu2 ->
                            menu2.isEnabled = false
                        }
                    }
                }
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            intent.getParcelableExtra(EXTRA_DETECT_RESULT, Predicts::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_DETECT_RESULT)
        }
        return when (item.itemId) {
            R.id.menu_collection -> {
                if (data != null){
                    collectData(data)
                    showToast(this, "Success save result")
                } else {
                    showDialog(this, "Cannot save result")
                }
                true
            }
            R.id.action_delete_collection -> {
                if (data != null){
                    viewModel.getCollectionByImage(data.image).observe(this){ result ->
                        if (result != null){
                            dialogDeleteAction(this, "Delete Collection", "Are you sure you want to delete Detection Result ?") {
                                viewModel.deleteCollection(result.image)
                            }
                        }
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setDataFromIntent() {
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
            } else {
                finish()
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