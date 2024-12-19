package com.sugara.submissionandroidintermediate.view.addStory

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.sugara.submissionandroidintermediate.data.UserRepository
import com.sugara.submissionandroidintermediate.data.api.ApiConfig
import com.sugara.submissionandroidintermediate.data.model.AddStoryModel
import com.sugara.submissionandroidintermediate.utils.reduceFileImage
import com.sugara.submissionandroidintermediate.utils.uriToFile
import com.sugara.submissionandroidintermediate.view.wrapper.ResponseWrapper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddStoryViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _response = MutableLiveData<ResponseWrapper>()
    val response: LiveData<ResponseWrapper> = _response


    fun addStory(desc: String, image: Uri,context : Context) {
        _isLoading.value = true
        val token = "Bearer ${userRepository.getToken()}"

        val descBody = desc.toRequestBody("text/plain".toMediaTypeOrNull())
        val photo = image?.let { uri ->
            val file = uriToFile(uri,context).reduceFileImage()
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("photo", file.name, requestFile)
        }

        val client = ApiConfig.getApiService().addStory(
            descBody,
            photo,
            token
        )
        client.enqueue(object : Callback<AddStoryModel> {
            override fun onResponse(
                call: Call<AddStoryModel>,
                response: Response<AddStoryModel>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.code() == 201) {
                    _response.value = ResponseWrapper(true, response.body()?.message ?: "Unknown success message")
                } else if (response.code() == 400) {
                    val errorResponse = response.errorBody()?.string()
                    val gson = Gson()
                    val errorModel = gson.fromJson(errorResponse, AddStoryModel::class.java)
                    _response.value = ResponseWrapper(false, errorModel.message ?: "Unknown error")
                } else {
                    _response.value = ResponseWrapper(false, "Unexpected error")
                }
            }
            override fun onFailure(call: Call<AddStoryModel>, t: Throwable) {
                _isLoading.value = false
                _response.value = ResponseWrapper(false, t.message ?: "Unknown error")
            }
        })

    }
}