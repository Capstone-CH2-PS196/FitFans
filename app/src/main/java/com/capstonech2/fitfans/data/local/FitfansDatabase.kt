package com.capstonech2.fitfans.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.capstonech2.fitfans.data.model.Collection
import com.capstonech2.fitfans.data.model.History

@Database(
    entities = [Collection::class, History::class],
    version = 1,
    exportSchema = false
)
abstract class FitfansDatabase : RoomDatabase() {
    abstract fun collectionDao() : CollectionDao
    abstract fun historyDao() : HistoryDao
}