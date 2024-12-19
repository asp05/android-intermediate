package com.sugara.submissionandroidintermediate.di

import android.content.Context
import com.sugara.submissionandroidintermediate.data.UserRepository
import com.sugara.submissionandroidintermediate.data.api.ApiConfig
import com.sugara.submissionandroidintermediate.data.pref.UserPreference
import com.sugara.submissionandroidintermediate.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}