package com.capstonech2.fitfans.ui.profile

import androidx.lifecycle.ViewModel
import com.capstonech2.fitfans.data.GymRepository

class ProfileViewModel(private val repository: GymRepository): ViewModel(){
    fun getUserDataByEmail(email: String) = repository.getUserByEmail(email)
}