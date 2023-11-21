package com.capstonech2.fitfans.ui.detectionresult

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstonech2.fitfans.databinding.RecommendationListBinding
import com.capstonech2.fitfans.model.Recommendation

class DetectionResultAdapter : ListAdapter<Recommendation, DetectionResultAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RecommendationListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    class MyViewHolder(private val itemBinding : RecommendationListBinding)
        : RecyclerView.ViewHolder(itemBinding.root){
            fun bind(data : Recommendation){
                itemBinding.recommendationLevel.text = data.level
                itemBinding.recommendationTime.text = data.time
            }
        }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Recommendation>() {
            override fun areItemsTheSame(oldItem: Recommendation, newItem: Recommendation): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Recommendation, newItem: Recommendation): Boolean {
                return oldItem == newItem
            }
        }
    }
}