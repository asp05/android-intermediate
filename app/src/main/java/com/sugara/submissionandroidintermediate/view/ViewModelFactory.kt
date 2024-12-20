package com.sugara.submissionandroidintermediate.view

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sugara.submissionandroidintermediate.data.UserRepository
import com.sugara.submissionandroidintermediate.di.Injection
import com.sugara.submissionandroidintermediate.view.addStory.AddStoryViewModel
import com.sugara.submissionandroidintermediate.view.home.HomeViewModel
import com.sugara.submissionandroidintermediate.view.maps.MapsViewModel
import com.sugara.submissionandroidintermediate.view.signin.SigninViewModel
import com.sugara.submissionandroidintermediate.view.signup.SignupViewModel

class ViewModelFactory private constructor(private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory() {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignupViewModel::class.java)) {
            return SignupViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(SigninViewModel::class.java)) {
            return SigninViewModel(userRepository) as T
        }else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(userRepository) as T
        }else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(userRepository) as T
        }else if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(userRepository) as T
        }else if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(userRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}