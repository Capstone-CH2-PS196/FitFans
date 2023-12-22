package com.capstonech2.fitfans.ui.history.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstonech2.fitfans.data.model.HistoryAndExercise
import com.capstonech2.fitfans.databinding.DetailHistoryItemBinding
import com.capstonech2.fitfans.utils.capitalizeFirstLetter
import com.capstonech2.fitfans.utils.convertMillisToMinutesSeconds
import com.capstonech2.fitfans.utils.getIconTools

class DetailHistoryAdapter : ListAdapter<HistoryAndExercise, DetailHistoryAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailHistoryAdapter.ViewHolder {
        val binding = DetailHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    inner class ViewHolder(private val itemBinding: DetailHistoryItemBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(data: HistoryAndExercise){
            itemBinding.historyCircleIcon.setImageResource(getIconTools(data.exercise.exeToolName.capitalizeFirstLetter()))
            itemBinding.toolName.text = data.exercise.exeToolName.capitalizeFirstLetter()
            itemBinding.timeValue.text = convertMillisToMinutesSeconds(data.exercise.exeTime)
            itemBinding.resultValue.text = String.format("%1\$s Cal", data.exercise.exeCal)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryAndExercise>(){
            override fun areItemsTheSame(oldItem: HistoryAndExercise, newItem: HistoryAndExercise): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: HistoryAndExercise, newItem: HistoryAndExercise): Boolean {
                return oldItem == newItem
            }
        }
    }
}