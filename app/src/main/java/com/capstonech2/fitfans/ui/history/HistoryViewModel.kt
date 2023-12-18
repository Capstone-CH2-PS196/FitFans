package com.capstonech2.fitfans.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstonech2.fitfans.data.HistoryRepository
import com.capstonech2.fitfans.data.model.Exercise
import com.capstonech2.fitfans.data.model.History
import com.capstonech2.fitfans.data.model.HistoryAndExercise
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {

    fun insertHistory(history: History) = viewModelScope.launch {
        repository.insertHistory(history)
    }

    fun insertExercise(exercise: Exercise) = viewModelScope.launch {
        repository.insertExercise(exercise)
    }

    fun updateHistoryChecked(hisId: Int, isChecked: Int) = viewModelScope.launch {
        repository.updateHistoryChecked(hisId, isChecked)
    }

    fun updateExerciseChecked(hisId: Int, isChecked: Int) = viewModelScope.launch {
        repository.updateExerciseChecked(hisId, isChecked)
    }

    fun updateAllHistoriesCheckedStatus(isChecked: Boolean) = viewModelScope.launch {
        val isCheckedValue = if (isChecked) 1 else 0
        repository.updateAllHistoriesCheckedStatus(isCheckedValue)
    }

    fun updateAllExerciseCheckedStatus(isChecked: Boolean) = viewModelScope.launch {
        val isCheckedValue = if (isChecked) 1 else 0
        repository.updateAllExerciseCheckedStatus(isCheckedValue)
    }

    fun deleteHistoryByChecked() = viewModelScope.launch {
        repository.deleteHistoryByChecked()
    }

    fun deleteExerciseByChecked() = viewModelScope.launch {
        repository.deleteExerciseByChecked()
    }

    fun getAllHistory() : LiveData<List<History>> = repository.getAllHistory()

    fun getHistoryByDate(date: String) : LiveData<History> = repository.getHistoryByDate(date)

    fun getAllExerciseByIdHistory(hisId: Int) : LiveData<List<HistoryAndExercise>> =
        repository.getAllExerciseByIdHistory(hisId)

    fun getTotalCaloriesExercise(hisId: Int): LiveData<Double> = repository.getTotalCaloriesExercise(hisId)

    fun getTotalTimeExercise(hisId: Int): LiveData<Long> = repository.getTotalTimeExercise(hisId)
}