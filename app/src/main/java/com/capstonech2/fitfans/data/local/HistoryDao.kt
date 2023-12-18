package com.capstonech2.fitfans.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.capstonech2.fitfans.data.model.Exercise
import com.capstonech2.fitfans.data.model.History
import com.capstonech2.fitfans.data.model.HistoryAndExercise

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertHistory(history: History)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertExercise(exercise: Exercise)

    @Query("SELECT * FROM tb_history WHERE date = :date")
    fun getHistoryByDate(date: String) : LiveData<History>

    @Query("SELECT * FROM tb_history")
    fun getAllHistory() : LiveData<List<History>>

    @Query("SELECT * FROM tb_exercise WHERE historyId = :hisId")
    fun getAllExerciseByIdHistory(hisId: Int) : LiveData<List<HistoryAndExercise>>

    @Query("SELECT SUM(exeCal) FROM tb_exercise WHERE historyId = :hisId")
    fun getTotalCaloriesExercise(hisId: Int): LiveData<Double>

    @Query("SELECT SUM(exeTime) FROM tb_exercise WHERE historyId = :hisId")
    fun getTotalTimeExercise(hisId: Int): LiveData<Long>
}