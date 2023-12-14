package com.capstonech2.fitfans.ui.note

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstonech2.fitfans.R
import com.capstonech2.fitfans.databinding.ActivityNoteBinding
import com.capstonech2.fitfans.ui.note.add.AddNoteActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class NoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteBinding
    private val viewModel: NoteViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = getString(R.string.note)
            setDisplayHomeAsUpEnabled(true)
        }

        binding.fabAddNote.setOnClickListener {
            startActivity(Intent(this, AddNoteActivity::class.java))
        }

        showAllNote()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.select_all_note -> {
                true
            }
            R.id.action_delete_note -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun showAllNote(){
        val layoutManager = LinearLayoutManager(this@NoteActivity)
        binding.listNote.layoutManager = layoutManager

        viewModel.getAllNote().observe(this){ listNote ->
            val adapter = NoteAdapter()
            adapter.submitList(listNote)
            binding.listNote.adapter = adapter
        }
    }
}