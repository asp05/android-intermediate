package com.sugara.submissionandroidintermediate.view.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.sugara.submissionandroidintermediate.data.UserRepository
import com.sugara.submissionandroidintermediate.data.model.LoginModel
import com.sugara.submissionandroidintermediate.data.model.LoginResult
import com.sugara.submissionandroidintermediate.di.ResultState
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SigninViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun login(email: String,password:String) = liveData {
        emit(ResultState.Loading)
        try {
            val message = userRepository.login(email, password)
            emit(ResultState.Success(message))
        }catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, LoginModel::class.java)
            emit(ResultState.Error(errorBody?.message ?: "Error"))
        }
    }

    fun saveSession(user: LoginResult) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }
}