package com.sugara.submissionandroidintermediate.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.sugara.submissionandroidintermediate.data.api.ApiService
import com.sugara.submissionandroidintermediate.data.model.AddStoryModel
import com.sugara.submissionandroidintermediate.data.model.DetailStoryResponse
import com.sugara.submissionandroidintermediate.data.model.ListStoryItem
import com.sugara.submissionandroidintermediate.data.model.LoginModel
import com.sugara.submissionandroidintermediate.data.model.LoginResult
import com.sugara.submissionandroidintermediate.data.model.RegisterModel
import com.sugara.submissionandroidintermediate.data.model.StoryModel
import com.sugara.submissionandroidintermediate.data.pref.UserPreference
import com.sugara.submissionandroidintermediate.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody


class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun saveSession(user: LoginResult) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<LoginResult> {
        return userPreference.getSession()
    }

    fun getToken(): String = runBlocking {
        userPreference.getSession().first().token ?: ""
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun register(name: String, email: String, password: String): RegisterModel {
        return apiService.register(name, email, password)
    }

    suspend fun login(email: String, password: String): LoginModel {
        return apiService.login(email, password)
    }

    fun getListStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PagingSource(apiService)
            }
        ).liveData
    }

    suspend fun storyDetail(id: String): DetailStoryResponse {
        return apiService.storyDetail(id)
    }

    suspend fun addStory(description: RequestBody, photo: MultipartBody.Part): AddStoryModel {
        return apiService.addStory(description, photo)
    }

    suspend fun getLocationStories(location: Int): StoryModel {
        return apiService.getStoriesLocation(location)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}