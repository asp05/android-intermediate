package com.sugara.submissionandroidintermediate.view.signin

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.sugara.submissionandroidintermediate.data.UserRepository
import com.sugara.submissionandroidintermediate.data.api.ApiConfig
import com.sugara.submissionandroidintermediate.data.model.LoginModel
import com.sugara.submissionandroidintermediate.data.model.LoginResult
import com.sugara.submissionandroidintermediate.data.model.UserModel
import com.sugara.submissionandroidintermediate.data.pref.UserPreference
import com.sugara.submissionandroidintermediate.data.pref.dataStore
import com.sugara.submissionandroidintermediate.view.wrapper.ResponseWrapper
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val mUserRepository: UserRepository = userRepository
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _response = MutableLiveData<ResponseWrapper>()
    val response: LiveData<ResponseWrapper> = _response

    fun login(user: UserModel) {
        _isLoading.value = true
        Log.d("LoginViewModel", "login: ${user}")
        val client = ApiConfig.getApiService().login(
            user.email ?: "",
            user.password ?: ""
        )
        client.enqueue(object : Callback<LoginModel> {
            override fun onResponse(
                call: Call<LoginModel>,
                response: Response<LoginModel>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.code() == 200) {
                    _response.value = ResponseWrapper(true, response.body()?.message ?: "Unknown success message")
                    viewModelScope.launch {
                        val body = response.body()?.loginResult
                        val userLogin = LoginResult(
                            userId = body?.userId,
                            name = body?.name,
                            token = body?.token,
                        )
                        mUserRepository.saveSession(userLogin)
                    }
                } else if (response.code() == 401) {
                    val errorResponse = response.errorBody()?.string()
                    val gson = Gson()
                    val errorModel = gson.fromJson(errorResponse, LoginModel::class.java)
                    _response.value = ResponseWrapper(false, errorModel.message ?: "Unknown error")
                } else {
                    _response.value = ResponseWrapper(false, "Unexpected error")
                }
            }

            override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                _isLoading.value = false
                _response.value = ResponseWrapper(false, t.message ?: "Unknown error")
            }
        })

    }
}