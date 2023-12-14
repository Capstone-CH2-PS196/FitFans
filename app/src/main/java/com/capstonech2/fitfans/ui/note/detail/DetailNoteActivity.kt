package com.capstonech2.fitfans.ui.note.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.data.model.Note
import com.capstonech2.fitfans.databinding.ActivityDetailNoteBinding
import com.capstonech2.fitfans.ui.note.NoteAdapter.Companion.EXTRA_NOTE_ID
import com.capstonech2.fitfans.ui.note.NoteViewModel
import com.capstonech2.fitfans.ui.note.edit.EditNoteActivity
import com.capstonech2.fitfans.utils.dialogDeleteAction
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailNoteBinding
    private val viewModel: NoteViewModel by viewModel()
    private var data: Int? = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = getString(R.string.detail_note)
            setDisplayHomeAsUpEnabled(true)
        }

        data = intent.getIntExtra(EXTRA_NOTE_ID, 0)
        data?.let { noteId ->
            viewModel.getNoteById(noteId).observe(this) { note ->
                note?.let {
                    showNote(note)
                } ?: run {
                    finish()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit_note -> {
                val intent = Intent(this, EditNoteActivity::class.java)
                intent.putExtra(EXTRA_NOTE_ID, data)
                startActivity(intent)
                true
            }
            R.id.action_delete_note -> {
                dialogDeleteAction(this, getString(R.string.delete_note), getString(R.string.delete_note_message)) {
                    data?.let { noteId ->
                        viewModel.getNoteById(noteId).observe(this) { note ->
                            note?.let {
                                viewModel.deleteNote(note)
                            }
                        }
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun showNote(note: Note) {
        binding.detailTitleNote.text = note.title
        binding.detailDateNote.text = note.date
        binding.detailDescriptionNote.text = note.description
    }
}