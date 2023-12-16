package com.capstonech2.fitfans.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.capstonech2.fitfans.data.model.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM tb_note ORDER BY id ASC")
    fun getAllNote() : LiveData<List<Note>>

    @Query("SELECT * FROM tb_note WHERE id = :noteId")
    fun getNoteById(noteId: Int) : LiveData<Note>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note: Note)

    @Query("UPDATE tb_note SET date = :date, title = :title, description = :description WHERE id = :noteId")
    suspend fun updateNote(noteId: Int, date: String, title: String, description: String)

    @Query("UPDATE tb_note SET isChecked = :isChecked WHERE id = :noteId")
    suspend fun updateNoteChecked(noteId: Int, isChecked: Int)

    @Query("UPDATE tb_note SET isChecked = :isChecked")
    suspend fun updateAllNotesCheckedStatus(isChecked: Int)

    @Query("DELETE FROM tb_note WHERE isChecked = 1")
    suspend fun deleteNoteByChecked()

    @Delete
    suspend fun deleteNote(note: Note)
}