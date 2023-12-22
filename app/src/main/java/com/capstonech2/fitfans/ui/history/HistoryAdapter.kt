package com.capstonech2.fitfans.ui.history

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstonech2.fitfans.data.model.History
import com.capstonech2.fitfans.databinding.HistoryItemBinding
import com.capstonech2.fitfans.ui.history.detail.DetailHistoryActivity
import com.capstonech2.fitfans.utils.EXTRA_HISTORY_ID
import com.capstonech2.fitfans.utils.getDayFromDate

class HistoryAdapter(private val onCheckboxClickListener: (History) -> Unit) : ListAdapter<History, HistoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data, onCheckboxClickListener)
    }

    inner class ViewHolder(private val itemBinding: HistoryItemBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(data : History, onCheckboxClickListener: (History) -> Unit){
            itemBinding.dayHistory.text = getDayFromDate(data.date)
            itemBinding.dateHistory.text = data.date

            val status = data.isChecked == 1
            itemBinding.checkboxHistory.isChecked = status

            itemBinding.checkboxHistory.setOnCheckedChangeListener { _, isChecked ->
                val updatedHistory = data.copy(isChecked = if (isChecked) 1 else 0)
                onCheckboxClickListener.invoke(updatedHistory)
            }

            itemBinding.historyLayout.setOnClickListener {
                val intent = Intent(it.context, DetailHistoryActivity::class.java)
                intent.putExtra(EXTRA_HISTORY_ID, data.hisId)
                it.context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<History>(){
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }
        }
    }
}