package com.capstonech2.fitfans.ui.detectionresult

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstonech2.fitfans.databinding.GuidanceItemBinding

class GuidanceAdapter : ListAdapter<String, GuidanceAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = GuidanceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(position + 1, data)
    }

    class MyViewHolder(private val itemBinding : GuidanceItemBinding)
        : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(number: Int, data : String){
            itemBinding.textViewNumber.text = number.toString()
            itemBinding.textViewContent.text = data
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}