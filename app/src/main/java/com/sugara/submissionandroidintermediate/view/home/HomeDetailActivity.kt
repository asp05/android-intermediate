package com.sugara.submissionandroidintermediate.view.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.sugara.submissionandroidintermediate.R
import com.sugara.submissionandroidintermediate.data.model.Story
import com.sugara.submissionandroidintermediate.databinding.ActivityAddStoryBinding
import com.sugara.submissionandroidintermediate.databinding.ActivityHomeDetailBinding
import com.sugara.submissionandroidintermediate.di.ResultState
import com.sugara.submissionandroidintermediate.view.ViewModelFactory
import com.sugara.submissionandroidintermediate.view.addStory.AddStoryViewModel

class HomeDetailActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "HomeDetailActivity"
        const val EXTRA_ID = "extra_id"
    }

    private lateinit var binding : ActivityHomeDetailBinding
    private lateinit var homeViewModel: HomeViewModel

    private lateinit var loadingDialog: android.app.AlertDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(EXTRA_ID) ?: ""
        homeViewModel = obtainViewModel(this)



        homeViewModel.getDetailStory(id).observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    showLoadingDialog()
                }
                is ResultState.Success -> {
                    dismissLoadingDialog()
                    val story = result.data as Story
                    binding.tvTitle.text = story.name
                    binding.tvDesc.text = story.description
                    Glide.with(this)
                        .load(story.photoUrl)
                        .into(binding.ivPicture)
                }
                is ResultState.Error -> {
                    dismissLoadingDialog()
                    AlertDialog.Builder(this).apply {
                        setTitle("Error")
                        setMessage(result.error)
                        setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                    }.show()
                }
            }
        }
    }



    private fun showLoadingDialog() {
        val builder = android.app.AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_loading, null)
        builder.setView(dialogView)
        builder.setCancelable(false)
        loadingDialog = builder.create()
        loadingDialog.show()
    }

    private fun dismissLoadingDialog() {
        if (::loadingDialog.isInitialized && loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }


    private fun obtainViewModel(activity: AppCompatActivity): HomeViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(HomeViewModel::class.java)
    }
}