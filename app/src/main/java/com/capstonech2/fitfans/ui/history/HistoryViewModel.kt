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

    fun getAllHistory() : LiveData<List<History>> = repository.getAllHistory()

    fun getHistoryByDate(date: String) : LiveData<History> = repository.getHistoryByDate(date)

    fun getAllExerciseByIdHistory(hisId: Int) : LiveData<List<HistoryAndExercise>> =
        repository.getAllExerciseByIdHistory(hisId)

    fun getTotalCaloriesExercise(hisId: Int): LiveData<Double> = repository.getTotalCaloriesExercise(hisId)

    fun getTotalTimeExercise(hisId: Int): LiveData<Long> = repository.getTotalTimeExercise(hisId)
}