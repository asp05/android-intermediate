package com.sugara.submissionandroidintermediate.view.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.sugara.submissionandroidintermediate.data.UserRepository
import com.sugara.submissionandroidintermediate.data.api.ApiConfig
import com.sugara.submissionandroidintermediate.data.model.DetailStoryResponse
import com.sugara.submissionandroidintermediate.data.model.ListStoryItem
import com.sugara.submissionandroidintermediate.data.model.LoginModel
import com.sugara.submissionandroidintermediate.data.model.LoginResult
import com.sugara.submissionandroidintermediate.data.model.Story
import com.sugara.submissionandroidintermediate.data.model.StoryModel
import com.sugara.submissionandroidintermediate.data.model.UserModel
import com.sugara.submissionandroidintermediate.data.pref.UserPreference
import com.sugara.submissionandroidintermediate.data.pref.dataStore
import com.sugara.submissionandroidintermediate.view.wrapper.ResponseWrapper
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _listStories = MutableLiveData<List<ListStoryItem>>()
    val listStories: LiveData<List<ListStoryItem>> = _listStories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _detailStory = MutableLiveData<Story>()
    val detailStory: LiveData<Story> = _detailStory

    fun getStories() {
        _isLoading.value = true
        val token = "Bearer ${userRepository.getToken()}"

        val client = ApiConfig.getApiService().stories(token)
        client.enqueue(object : Callback<StoryModel> {
            override fun onResponse(
                call: Call<StoryModel>,
                response: Response<StoryModel>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listStories.value = responseBody.listStory as List<ListStoryItem>
                    }
                }
            }
            override fun onFailure(call: Call<StoryModel>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    fun getDetailStory(id: String) {
        _isLoading.value = true
        val token = "Bearer ${userRepository.getToken()}"

        val client = ApiConfig.getApiService().storyDetail(id, token)
        client.enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _detailStory.value = responseBody.story as Story
                    }
                }
            }
            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}