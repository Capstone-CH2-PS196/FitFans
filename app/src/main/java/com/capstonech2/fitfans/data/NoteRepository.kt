package com.capstonech2.fitfans.data

import androidx.lifecycle.LiveData
import com.capstonech2.fitfans.data.local.FitfansDatabase
import com.capstonech2.fitfans.data.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepository(private val database: FitfansDatabase) {

    fun getAllNote(): LiveData<List<Note>> = database.noteDao().getAllNote()

    fun getNoteById(noteId: Int): LiveData<Note> = database.noteDao().getNoteById(noteId)

    suspend fun insertNewNote(note: Note) = withContext(Dispatchers.IO) {
        database.noteDao().insertNote(note)
    }

    suspend fun updateNote(noteId: Int, date: String, title: String, description: String) = withContext(Dispatchers.IO) {
        database.noteDao().updateNote(noteId, date, title, description)
    }

    suspend fun updateAllNoteStatusChecked(isChecked: Int) = withContext(Dispatchers.IO){
        database.noteDao().updateAllNotesCheckedStatus(isChecked)
    }

    suspend fun updateNoteCheckedStatus(noteId: Int, isChecked: Int) = withContext(Dispatchers.IO){
        database.noteDao().updateNoteChecked(noteId, isChecked)
    }

    suspend fun deleteNoteByCheckedStatus() = withContext(Dispatchers.IO) {
        database.noteDao().deleteNoteByChecked()
    }

    suspend fun deleteNote(note: Note) = withContext(Dispatchers.IO) {
        database.noteDao().deleteNote(note)
    }
}