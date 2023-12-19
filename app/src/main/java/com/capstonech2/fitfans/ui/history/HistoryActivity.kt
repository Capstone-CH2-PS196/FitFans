package com.capstonech2.fitfans.ui.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.databinding.ActivityHistoryBinding
import com.capstonech2.fitfans.utils.dialogDeleteAction
import com.capstonech2.fitfans.utils.show
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.select_all_note -> {
                viewModel.updateAllHistoriesCheckedStatus(true)
                viewModel.updateAllExerciseCheckedStatus(true)
                true
            }
            R.id.action_delete_note -> {
                dialogDeleteAction(this,
                    getString(R.string.delete_history),
                    getString(R.string.delete_selected_history_message)
                ){
                    viewModel.deleteHistoryByChecked()
                    viewModel.deleteExerciseByChecked()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showAllHistories() {
        val layoutManager = LinearLayoutManager(this@HistoryActivity)
        binding.listHistory.layoutManager = layoutManager

        viewModel.getAllHistory().observe(this){ listHistory ->
            if(listHistory.isNotEmpty()){
                binding.emptyHistoryText.show(false)
                val adapter = HistoryAdapter{
                    viewModel.updateHistoryChecked(it.hisId, it.isChecked)
                    viewModel.updateExerciseChecked(it.hisId, it.isChecked)
                }
                adapter.submitList(listHistory)
                binding.listHistory.adapter = adapter
            } else {
                binding.emptyHistoryText.show(true)
            }
        }
    }
}