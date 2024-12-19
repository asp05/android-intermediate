package com.sugara.submissionandroidintermediate.view.signup

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.sugara.submissionandroidintermediate.data.UserRepository
import com.sugara.submissionandroidintermediate.data.api.ApiConfig
import com.sugara.submissionandroidintermediate.data.model.RegisterModel
import com.sugara.submissionandroidintermediate.data.model.UserModel
import com.sugara.submissionandroidintermediate.view.wrapper.ResponseWrapper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _response = MutableLiveData<ResponseWrapper>()
    val response: LiveData<ResponseWrapper> = _response

    fun register(user : UserModel) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().register(
            user.name ?: "",
            user.email ?: "",
            user.password ?: ""
        )
        client.enqueue(object : Callback<RegisterModel> {
            override fun onResponse(
                call: Call<RegisterModel>,
                response: Response<RegisterModel>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.code() == 201) {
                    _response.value = ResponseWrapper(true, response.body()?.message ?: "Unknown success message")
                } else if (response.code() == 400) {
                    val errorResponse = response.errorBody()?.string()
                    val gson = Gson()
                    val errorModel = gson.fromJson(errorResponse, RegisterModel::class.java)
                    _response.value = ResponseWrapper(false, errorModel.message ?: "Unknown error")
                } else {
                    _response.value = ResponseWrapper(false, "Unexpected error")
                }
            }
            override fun onFailure(call: Call<RegisterModel>, t: Throwable) {
                _isLoading.value = false
                _response.value = ResponseWrapper(false, t.message ?: "Unknown error")
            }
        })
    }
}