package com.sugara.submissionandroidintermediate.view.addStory

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.sugara.submissionandroidintermediate.data.UserRepository
import com.sugara.submissionandroidintermediate.data.model.AddStoryModel
import com.sugara.submissionandroidintermediate.data.model.LoginModel
import com.sugara.submissionandroidintermediate.di.ResultState
import com.sugara.submissionandroidintermediate.utils.reduceFileImage
import com.sugara.submissionandroidintermediate.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException


class AddStoryViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun addStory(desc: String, image: Uri,context : Context) = liveData {
        emit(ResultState.Loading)
        val descBody = desc.toRequestBody("text/plain".toMediaTypeOrNull())
        val photo = image?.let { uri ->
            val file = uriToFile(uri,context).reduceFileImage()
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("photo", file.name, requestFile)
        }

        try {
            val message = userRepository.addStory(descBody, photo!!)
            emit(ResultState.Success(message))
        }catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, AddStoryModel::class.java)
            emit(ResultState.Error(errorBody?.message ?: "Error"))
        }

    }
}