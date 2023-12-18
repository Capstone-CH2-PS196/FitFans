package com.capstonech2.fitfans.ui.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.databinding.ActivityHistoryBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private val viewModel : HistoryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = getString(R.string.history)
            setDisplayHomeAsUpEnabled(true)
        }
        showAllHistories()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun showAllHistories() {
        val layoutManager = LinearLayoutManager(this@HistoryActivity)
        binding.listHistory.layoutManager = layoutManager

        viewModel.getAllHistory().observe(this){ listHistory ->
            val adapter = HistoryAdapter()
            adapter.submitList(listHistory)
            binding.listHistory.adapter = adapter
        }
    }
}