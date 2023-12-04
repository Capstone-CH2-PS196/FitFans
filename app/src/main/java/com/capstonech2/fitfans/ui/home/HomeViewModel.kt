package com.capstonech2.fitfans.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstonech2.fitfans.data.GymRepository
import com.capstonech2.fitfans.data.remote.response.UsersResponseItem
import com.capstonech2.fitfans.utils.State
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: GymRepository) : ViewModel() {
    private val _userData = MutableLiveData<State<List<UsersResponseItem>>>()
    val userData: LiveData<State<List<UsersResponseItem>>> get() = _userData

    fun checkUserData(email :String) = viewModelScope.launch {
        try {
            _userData.value = State.Loading
            val response = repository.getUserByEmail(email)
            _userData.value = State.Success(response)
        } catch (e: Exception) {
            val errorMessage = e.localizedMessage ?: "Terjadi kesalahan yang tidak diketahui"
            _userData.value = State.Error(errorMessage)
        }
    }
}