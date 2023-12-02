package com.capstonech2.fitfans.ui.auth.login

import androidx.lifecycle.ViewModel
import com.capstonech2.fitfans.data.GymRepository

class LoginViewModel(private val repository: GymRepository) :ViewModel(){
    fun checkUserData(email :String) = repository.getUserByEmail(email)
}