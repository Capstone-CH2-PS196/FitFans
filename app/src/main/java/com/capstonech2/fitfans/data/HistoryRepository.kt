package com.capstonech2.fitfans.data

import androidx.lifecycle.LiveData
import com.capstonech2.fitfans.data.local.FitfansDatabase
import com.capstonech2.fitfans.data.model.Exercise
import com.capstonech2.fitfans.data.model.History
import com.capstonech2.fitfans.data.model.HistoryAndExercise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HistoryRepository(private val database: FitfansDatabase) {
    suspend fun insertHistory(history: History) = withContext(Dispatchers.IO) {
        database.historyDao().insertHistory(history)
    }

    suspend fun insertExercise(exercise: Exercise) = withContext(Dispatchers.IO) {
        database.historyDao().insertExercise(exercise)
    }

    suspend fun updateHistoryChecked(hisId: Int, isChecked: Int) = withContext(Dispatchers.IO) {
        database.historyDao().updateHistoryChecked(hisId, isChecked)
    }

    suspend fun updateExerciseChecked(hisId: Int, isChecked: Int) = withContext(Dispatchers.IO) {
        database.historyDao().updateExerciseChecked(hisId, isChecked)
    }

    suspend fun updateAllHistoriesCheckedStatus(isChecked: Int) = withContext(Dispatchers.IO) {
        database.historyDao().updateAllHistoriesCheckedStatus(isChecked)
    }

    suspend fun updateAllExerciseCheckedStatus(isChecked: Int) = withContext(Dispatchers.IO) {
        database.historyDao().updateAllExerciseCheckedStatus(isChecked)
    }

    suspend fun deleteHistoryByChecked() = withContext(Dispatchers.IO) {
        database.historyDao().deleteHistoryByChecked()
    }

    suspend fun deleteExerciseByChecked() = withContext(Dispatchers.IO) {
        database.historyDao().deleteExerciseByChecked()
    }

    fun getHistoryByDate(date: String) : LiveData<History> = database.historyDao().getHistoryByDate(date)

    fun getAllHistory() : LiveData<List<History>> = database.historyDao().getAllHistory()

    fun getAllExerciseByIdHistory(hisId: Int) : LiveData<List<HistoryAndExercise>> =
        database.historyDao().getAllExerciseByIdHistory(hisId)

    fun getTotalCaloriesExercise(hisId: Int): LiveData<Double> =
        database.historyDao().getTotalCaloriesExercise(hisId)

    fun getTotalTimeExercise(hisId: Int): LiveData<Long> = database.historyDao().getTotalTimeExercise(hisId)

    fun getTotalCaloriesBurnUser(): LiveData<Double> = database.historyDao().getTotalCaloriesBurnUser()
}