package com.sugara.submissionandroidintermediate.view.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.sugara.submissionandroidintermediate.data.UserRepository
import com.sugara.submissionandroidintermediate.data.model.RegisterModel
import com.sugara.submissionandroidintermediate.di.ResultState
import retrofit2.HttpException


class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun register(name: String, email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val message = userRepository.register(name, email, password)
            emit(ResultState.Success(message))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, RegisterModel::class.java)
            emit(ResultState.Error(errorBody.message ?: "Error"))
        }
    }
}