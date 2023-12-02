package com.capstonech2.fitfans.ui.auth.basicinformation

import androidx.lifecycle.ViewModel
import com.capstonech2.fitfans.data.GymRepository
import com.capstonech2.fitfans.data.model.User
import com.capstonech2.fitfans.data.remote.response.UsersResponseItem

class BasicInformationViewModel(private val repository: GymRepository) : ViewModel(){
    fun insertUser(data: UsersResponseItem) = repository.insertUser(data)
}