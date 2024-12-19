package com.sugara.submissionandroidintermediate.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.sugara.submissionandroidintermediate.data.UserRepository
import com.sugara.submissionandroidintermediate.data.model.LoginResult

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<LoginResult> {
        return userRepository.getSession().asLiveData()
    }
}