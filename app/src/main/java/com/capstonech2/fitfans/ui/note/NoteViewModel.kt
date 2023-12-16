package com.capstonech2.fitfans.ui.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstonech2.fitfans.data.NoteRepository
import com.capstonech2.fitfans.data.model.Note
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {
    fun getAllNote() = repository.getAllNote()

    fun getNoteById(noteId: Int) = repository.getNoteById(noteId)

    fun insertNote(note: Note) = viewModelScope.launch {
        repository.insertNewNote(note)
    }

    fun updateNote(noteId: Int, date: String, title: String, description: String) = viewModelScope.launch {
        repository.updateNote(noteId, date, title, description)
    }

    fun updateAllNoteStatusChecked(isChecked: Boolean) = viewModelScope.launch {
        val isCheckedValue = if (isChecked) 1 else 0
        repository.updateAllNoteStatusChecked(isCheckedValue)
    }

    fun updateNoteCheckedStatus(noteId: Int, isChecked: Int) = viewModelScope.launch {
        repository.updateNoteCheckedStatus(noteId, isChecked)
    }

    fun deleteNoteByCheckedStatus() = viewModelScope.launch {
        repository.deleteNoteByCheckedStatus()
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        repository.deleteNote(note)
    }
}