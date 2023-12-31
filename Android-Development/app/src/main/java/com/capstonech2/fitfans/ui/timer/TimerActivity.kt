package com.capstonech2.fitfans.ui.timer

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.IntentCompat.getParcelableExtra
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.data.model.Exercise
import com.capstonech2.fitfans.data.model.History
import com.capstonech2.fitfans.data.model.Predicts
import com.capstonech2.fitfans.data.model.Recommendation
import com.capstonech2.fitfans.data.model.TotalCalories
import com.capstonech2.fitfans.databinding.ActivityTimerBinding
import com.capstonech2.fitfans.ui.detectionresult.DetectionResultActivity
import com.capstonech2.fitfans.ui.history.HistoryViewModel
import com.capstonech2.fitfans.ui.profile.ProfileViewModel
import com.capstonech2.fitfans.utils.EXTRA_PREDICT
import com.capstonech2.fitfans.utils.EXTRA_RECOMMENDATION
import com.capstonech2.fitfans.utils.FITFANS_TITLE
import com.capstonech2.fitfans.utils.State
import com.capstonech2.fitfans.utils.calculateCalories
import com.capstonech2.fitfans.utils.capitalizeFirstLetter
import com.capstonech2.fitfans.utils.convertToMinutes
import com.capstonech2.fitfans.utils.formatDate
import com.capstonech2.fitfans.utils.show
import com.capstonech2.fitfans.utils.showToast
import com.capstonech2.fitfans.utils.showDialog
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Date

class TimerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTimerBinding
    private lateinit var auth: FirebaseAuth

    private val viewModel: TimerViewModel by viewModel()
    private val historyViewModel: HistoryViewModel by viewModel()
    private val profileViewModel: ProfileViewModel by viewModel()

    private var weight: Double = 0.0

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                showToast(this, getString(R.string.notification_granted))
            } else {
                showToast(this, getString(R.string.notification_not_granted))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        setContentView(binding.root)
        getWeightUser(auth.currentUser?.email.toString())

        supportActionBar?.apply {
            title = getString(R.string.timer)
            setDisplayHomeAsUpEnabled(true)
        }

        if (Build.VERSION.SDK_INT > 32) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        setData()
        updateDialogUI()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun getWeightUser(email: String) {
        profileViewModel.checkUserData(email)
        profileViewModel.userData.observe(this){
            if(it != null){
                when(it){
                    is State.Loading -> binding.loadingIndicator.show(true)
                    is State.Success -> {
                        binding.loadingIndicator.show(false)
                        weight = it.data[0].weight
                    }
                    is State.Error -> {
                        binding.loadingIndicator.show(false)
                        showDialog(this, "Cannot get user information, Please try again.")
                    }
                }
            }
        }
    }

    private fun setData() {
        val predict = getParcelableExtra(intent, EXTRA_PREDICT, Predicts::class.java)
        val recommendation = getParcelableExtra(intent,EXTRA_RECOMMENDATION, Recommendation::class.java)

        if(predict != null && recommendation != null){
            binding.tvTitle.text = recommendation.level
            binding.tvRecommendation.text = String.format(
                getString(R.string.timer_recommendation_temp), recommendation.set
            )

            viewModel.setInitialTime(recommendation.time)
            viewModel.currentTimeString.observe(this){ currentTime ->
                binding.tvTimer.text = currentTime
            }

            viewModel.buttonStart.observe(this){
                binding.btStart.isEnabled = it
            }

            viewModel.buttonPause.observe(this){
                binding.btPause.isEnabled = it
            }

            viewModel.buttonStop.observe(this){
                binding.btReset.isEnabled = it
            }

            viewModel.eventCountDownFinish.observe(this){
                if(it){
                    val stopTime = viewModel.getFinalTime()
                    if (stopTime != null && weight != 0.0){
                        val date = Date()
                        historyViewModel.getHistoryByDate(formatDate(date)).observe(this){ history ->
                            if(history != null){
                                historyViewModel.insertExercise(
                                    Exercise(
                                        exeToolName = predict.toolName,
                                        exeCal = calculateCalories(
                                            minutes = convertToMinutes(stopTime),
                                            equipment = predict.toolName.capitalizeFirstLetter(),
                                            level = recommendation.level,
                                            weight = weight
                                        ),
                                        exeTime = stopTime,
                                        historyId = history.hisId
                                    )
                                )
                                updateTotalCalories(auth.currentUser?.email.toString())
                            }
                        }
                    }
                }
            }

            viewModel.eventTimeUp.observe(this){
                if (it){
                    val data = workDataOf(
                        FITFANS_TITLE to predict.toolName
                    )
                    val request = OneTimeWorkRequestBuilder<NotificationWorker>()
                        .setInputData(data)
                        .build()
                    WorkManager.getInstance(this).enqueue(request)
                }
            }

            binding.btStart.setOnClickListener {
                viewModel.startTimer()

                val date = Date()
                historyViewModel.getHistoryByDate(formatDate(date)).observe(this){ history ->
                    if (history == null){
                        historyViewModel.insertHistory(History(date = formatDate(date)))
                    }
                }
            }

            binding.btPause.setOnClickListener {
                viewModel.pauseTimer()
            }

            binding.btReset.setOnClickListener {
                viewModel.resetTimer()
                startActivity(Intent(this@TimerActivity, DetectionResultActivity::class.java))
                finish()
            }
        }
    }

    private fun updateDialogUI(){
        binding.btOk.setOnClickListener {
            binding.btOk.visibility = View.GONE
            binding.cardView.setCardBackgroundColor(Color.GRAY)
        }
    }

    private fun updateTotalCalories(email: String) {
        historyViewModel.getTotalCaloriesBurnUser().observe(this){
            val total = TotalCalories(total_calories = it)
            profileViewModel.updateTotalCaloriesUser(email, total).observe(this){ state ->
                if (state != null) {
                    when(state){
                        is State.Loading -> binding.loadingIndicator.show(true)
                        is State.Success -> {
                            binding.loadingIndicator.show(false)
                            showToast(this, "Success update user data")
                        }
                        is State.Error -> {
                            binding.loadingIndicator.show(false)
                            showDialog(this,"Unknown error occurred, failed to update user data")
                        }
                    }
                }
            }
        }
    }
}