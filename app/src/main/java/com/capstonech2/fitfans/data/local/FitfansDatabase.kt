package com.capstonech2.fitfans.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.capstonech2.fitfans.data.model.Collection
import com.capstonech2.fitfans.data.model.History
import com.capstonech2.fitfans.data.model.Note
import com.capstonech2.fitfans.utils.StringListConverter

@Database(
    entities = [
        Note::class,
        Collection::class,
        History::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(StringListConverter::class)
abstract class FitfansDatabase : RoomDatabase() {
    abstract fun noteDao() : NoteDao
    abstract fun collectionDao() : CollectionDao
    abstract fun historyDao() : HistoryDao
}