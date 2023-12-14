package com.capstonech2.fitfans.ui.note.edit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.data.model.Note
import com.capstonech2.fitfans.databinding.ActivityEditNoteBinding
import com.capstonech2.fitfans.ui.note.NoteAdapter
import com.capstonech2.fitfans.ui.note.NoteViewModel
import com.capstonech2.fitfans.utils.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditNoteBinding
    private val viewModel: NoteViewModel by viewModel()
    private var data: Int? = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = getString(R.string.edit_note)
            setDisplayHomeAsUpEnabled(true)
        }

        data = intent.getIntExtra(NoteAdapter.EXTRA_NOTE_ID, 0)
        viewModel.getNoteById(data!!).observe(this){ note ->
            setData(note)
        }

        binding.buttonEditNote.setOnClickListener { updateNote() }
    }

    private fun setData(note: Note){
        binding.edEditTitle.setText(note.title)
        binding.edEditDescription.setText(note.description)
    }

    private fun updateNote(){
        binding.apply {
            val currentDate = Date()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(currentDate)
            val title = edEditTitle.text.toString()
            val description = edEditDescription.text.toString()

            data?.let {
                viewModel.updateNote(
                    noteId = it,
                    date = formattedDate.toString(),
                    title = title,
                    description = description
                )
                onBackPressedDispatcher.onBackPressed()
                finish()
            } ?: showToast(this@EditNoteActivity, "ID null")
        }
    }
}