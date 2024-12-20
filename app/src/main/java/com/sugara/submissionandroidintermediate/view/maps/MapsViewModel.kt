package com.sugara.submissionandroidintermediate.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.sugara.submissionandroidintermediate.data.UserRepository
import com.sugara.submissionandroidintermediate.data.model.DetailStoryResponse
import com.sugara.submissionandroidintermediate.data.model.ListStoryItem
import com.sugara.submissionandroidintermediate.data.model.LoginResult
import com.sugara.submissionandroidintermediate.data.model.Story
import com.sugara.submissionandroidintermediate.data.model.StoryModel
import com.sugara.submissionandroidintermediate.di.ResultState
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MapsViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getStories() = liveData {
        emit(ResultState.Loading)
        try {
            val message = userRepository.getLocationStories(1)
            emit(ResultState.Success(message))
        }catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, StoryModel::class.java)
            emit(ResultState.Error(errorBody?.message ?: "Error"))
        }
    }

}