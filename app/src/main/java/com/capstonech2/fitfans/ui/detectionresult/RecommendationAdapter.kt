package com.capstonech2.fitfans.ui.detectionresult

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstonech2.fitfans.data.model.Predicts
import com.capstonech2.fitfans.data.model.Recommendation
import com.capstonech2.fitfans.databinding.RecommendationListBinding
import com.capstonech2.fitfans.ui.timer.TimerActivity
import com.capstonech2.fitfans.utils.EXTRA_PREDICT
import com.capstonech2.fitfans.utils.EXTRA_RECOMMENDATION

class RecommendationAdapter(private val predict: Predicts) : ListAdapter<Recommendation, RecommendationAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RecommendationListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data, predict)
    }

    inner class MyViewHolder(private val itemBinding : RecommendationListBinding)
        : RecyclerView.ViewHolder(itemBinding.root){
            fun bind(data : Recommendation, result: Predicts){
                itemBinding.recommendationLevel.text = data.level
                itemBinding.recommendationTime.text = data.time.toString()
                itemBinding.buttonTimer.setOnClickListener {
                    val intent = Intent(it.context, TimerActivity::class.java)
                    intent.putExtra(EXTRA_PREDICT, result)
                    intent.putExtra(EXTRA_RECOMMENDATION, data)
                    it.context.startActivity(intent)
                }
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