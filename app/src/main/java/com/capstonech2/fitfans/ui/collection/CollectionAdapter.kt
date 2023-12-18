package com.capstonech2.fitfans.ui.collection

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstonech2.fitfans.databinding.CollectionItemBinding
import com.capstonech2.fitfans.data.model.Collection
import com.capstonech2.fitfans.data.model.Predicts
import com.capstonech2.fitfans.data.model.TimerRecommendation
import com.capstonech2.fitfans.ui.detectionresult.DetectionResultActivity
import com.capstonech2.fitfans.utils.EXTRA_DETECT_RESULT
import com.capstonech2.fitfans.utils.capitalizeFirstLetter
import com.capstonech2.fitfans.utils.loadImage

class CollectionAdapter: ListAdapter<Collection, CollectionAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CollectionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data)
    }

    class ViewHolder(private val itemBinding: CollectionItemBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(data: Collection){
            itemBinding.imageEquipment.loadImage(data.image)
            itemBinding.titleEquipment.text = data.toolName.capitalizeFirstLetter()
            itemBinding.descEquipment.text = data.toolDescription
            itemView.setOnClickListener {
                val intent = Intent(it.context, DetectionResultActivity::class.java)
                val recommendation = TimerRecommendation(
                    expert = data.timerRecommendation.expert,
                    ideal = data.timerRecommendation.ideal,
                    beginner = data.timerRecommendation.beginner
                )
                val resultPredict = Predicts(
                    image = data.image,
                    timerRecommendation = recommendation,
                    toolName = data.toolName,
                    howToUse = data.howToUse,
                    toolDescription = data.toolDescription
                )
                intent.putExtra(EXTRA_DETECT_RESULT, resultPredict)
                it.context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Collection>(){
            override fun areItemsTheSame(oldItem: Collection, newItem: Collection): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: Collection, newItem: Collection): Boolean {
                return oldItem == newItem
            }
        }
    }
}