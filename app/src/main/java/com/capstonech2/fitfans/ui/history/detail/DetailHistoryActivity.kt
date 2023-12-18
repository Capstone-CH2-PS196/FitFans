package com.capstonech2.fitfans.ui.history.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonech2.fitfans.databinding.ActivityDetailHistoryBinding
import com.capstonech2.fitfans.ui.history.HistoryViewModel
import com.capstonech2.fitfans.utils.convertMillisToMinutesSeconds
import com.capstonech2.fitfans.utils.getDayFromDate
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailHistoryBinding
    private val viewModel : HistoryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Detail History"
            setDisplayHomeAsUpEnabled(true)
        }

        setData()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setData() {
        val data = intent.getIntExtra("extra_historyId", 0)
        viewModel.getTotalCaloriesExercise(data).observe(this){
            binding.totalResultValue.text = String.format("%1\$s Cal", it)
        }

        viewModel.getTotalTimeExercise(data).observe(this) {
            binding.totalTimeValue.text = convertMillisToMinutesSeconds(it)
        }

        val layoutManager = LinearLayoutManager(this@DetailHistoryActivity)
        binding.listExercise.layoutManager = layoutManager
        viewModel.getAllExerciseByIdHistory(data).observe(this) { result ->
            if (result != null) {
                binding.detailHistoryDate.text = "${result[0].history?.date?.let { getDayFromDate(it) }}, ${result[0].history?.date}"
                val adapter = DetailHistoryAdapter()
                adapter.submitList(result)
                binding.listExercise.adapter = adapter
            }
        }
    }
}