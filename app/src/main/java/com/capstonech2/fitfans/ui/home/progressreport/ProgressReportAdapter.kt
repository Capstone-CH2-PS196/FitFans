package com.capstonech2.fitfans.ui.home.progressreport

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstonech2.fitfans.databinding.ReportItemBinding

class ProgressReportAdapter : ListAdapter<Report, ProgressReportAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ReportItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    class MyViewHolder(private val itemBinding : ReportItemBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(data : Report){
            itemBinding.reportIcon.setImageResource(data.icon)
            itemBinding.reportTitle.text = data.name
            itemBinding.reportValue.text = data.value.toString()
            itemBinding.reportUnit.text = data.unit
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Report>() {
            override fun areItemsTheSame(oldItem: Report, newItem: Report): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Report, newItem: Report): Boolean {
                return oldItem == newItem
            }
        }
    }
}