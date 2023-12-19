package com.capstonech2.fitfans.ui.note.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.data.model.Note
import com.capstonech2.fitfans.databinding.ActivityAddNoteBinding
import com.capstonech2.fitfans.ui.note.NoteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private val viewModel: NoteViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = getString(R.string.create_note)
            setDisplayHomeAsUpEnabled(true)
        }
        binding.buttonAddNote.setOnClickListener { addNote() }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun addNote(){
        binding.apply {
            val currentDate = Date()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(currentDate)
            val title = edAddTitle.text.toString()
            val description = edAddDescription.text.toString()

            val titleError = if (title.isEmpty()) "Title cannot be empty" else null
            val descriptionError = if (description.isEmpty()) "Description cannot be empty" else null

            if (titleError == null && descriptionError == null) {
                viewModel.insertNote(
                    Note(
                        date = formattedDate.toString(),
                        title = title,
                        description = description
                    )
                )
                onBackPressedDispatcher.onBackPressed()
                finish()
            } else {
                edAddTitleLayout.error = titleError
                edAddDescription.error = descriptionError
            }
        }
    }
}