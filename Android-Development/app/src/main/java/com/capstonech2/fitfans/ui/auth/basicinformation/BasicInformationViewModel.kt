package com.capstonech2.fitfans.ui.auth.basicinformation

import androidx.lifecycle.ViewModel
import com.capstonech2.fitfans.data.UsersRepository
import com.capstonech2.fitfans.data.model.User

class BasicInformationViewModel(private val repository: UsersRepository) : ViewModel(){
    fun insertUser(data: User) = repository.insertUser(data)
}