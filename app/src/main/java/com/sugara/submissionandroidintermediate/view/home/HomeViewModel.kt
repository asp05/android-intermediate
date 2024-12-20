package com.sugara.submissionandroidintermediate.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
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

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<LoginResult> {
        return userRepository.getSession().asLiveData()
    }

    val story: LiveData<PagingData<ListStoryItem>> =
        userRepository.getListStory().cachedIn(viewModelScope)

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    fun getDetailStory(id: String) = liveData {
        emit(ResultState.Loading)
        try {
            val message = userRepository.storyDetail(id)
            emit(ResultState.Success(message.story))
        }catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, DetailStoryResponse::class.java)
            emit(ResultState.Error(errorBody?.message ?: "Error"))
        }
    }
}