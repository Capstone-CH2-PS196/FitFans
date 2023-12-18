package com.capstonech2.fitfans.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstonech2.fitfans.data.UsersRepository
import com.capstonech2.fitfans.data.model.User
import com.capstonech2.fitfans.data.remote.response.UsersResponseItem
import com.capstonech2.fitfans.utils.State
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UsersRepository): ViewModel(){
    private val _userData = MutableLiveData<State<List<UsersResponseItem>>>()
    val userData: LiveData<State<List<UsersResponseItem>>> get() = _userData

    fun checkUserData(email :String) = viewModelScope.launch {
        try {
            _userData.value = State.Loading
            val response = repository.getUserByEmail(email)
            _userData.value = State.Success(response)
        } catch (e: Exception) {
            val errorMessage = e.localizedMessage ?: "Unknown error occurred"
            _userData.value = State.Error(errorMessage)
        }
    }

    fun updateUserByEmail(email: String, data: User) = repository.updateUserByEmail(email, data)
}