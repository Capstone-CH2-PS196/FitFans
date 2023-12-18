package com.capstonech2.fitfans.ui.note

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.capstonech2.fitfans.data.model.Note
import com.capstonech2.fitfans.databinding.NoteItemBinding
import com.capstonech2.fitfans.ui.note.detail.DetailNoteActivity
import com.capstonech2.fitfans.utils.EXTRA_NOTE_ID
import com.capstonech2.fitfans.utils.capitalizeFirstLetter

class NoteAdapter(
    private val onCheckboxClickListener: (Note) -> Unit
): ListAdapter<Note, NoteAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.bind(data, onCheckboxClickListener)
    }

    inner class ViewHolder(private val itemBinding : NoteItemBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(data : Note, onCheckboxClickListener: (Note) -> Unit){
            itemBinding.titleNote.text = data.title.capitalizeFirstLetter()
            itemBinding.dateNote.text = data.date

            val status = data.isChecked == 1
            itemBinding.checkboxNote.isChecked = status

            itemBinding.checkboxNote.setOnCheckedChangeListener { _, isChecked ->
                val updatedNote = data.copy(isChecked = if (isChecked) 1 else 0)
                onCheckboxClickListener.invoke(updatedNote)
            }

            itemBinding.noteListLayout.setOnClickListener {
                val intent = Intent(it.context, DetailNoteActivity::class.java)
                intent.putExtra(EXTRA_NOTE_ID, data.id)
                it.context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Note>(){
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem == newItem
            }
        }
    }
}