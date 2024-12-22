package com.sugara.submissionandroidintermediate.di

import android.content.Context
import android.util.Log
import com.sugara.submissionandroidintermediate.data.UserRepository
import com.sugara.submissionandroidintermediate.data.api.ApiConfig
import com.sugara.submissionandroidintermediate.data.pref.UserPreference
import com.sugara.submissionandroidintermediate.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        Log.d("Injection", "provideRepository: ${user.token}")
        val apiService = ApiConfig.getApiService(user.token?:"")
        return UserRepository.getInstance(pref, apiService)
    }
}