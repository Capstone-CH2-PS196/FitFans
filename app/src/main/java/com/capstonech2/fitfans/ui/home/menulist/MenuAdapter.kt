package com.capstonech2.fitfans.ui.home.menulist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.capstonech2.fitfans.databinding.MenuListItemBinding

class MenuAdapter(private val menuList: List<Menu>)
    : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MenuListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = menuList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = menuList[position]
        holder.bind(data)
    }

    class ViewHolder(private val itemBinding: MenuListItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Menu){
            itemBinding.iconMenu.setImageResource(data.icon)
            itemBinding.titleMenu.text = data.name
        }
    }
}