package com.sugara.submissionandroidintermediate.view.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sugara.submissionandroidintermediate.R
import com.sugara.submissionandroidintermediate.adapter.StoryAdapter
import com.sugara.submissionandroidintermediate.data.model.ListStoryItem
import com.sugara.submissionandroidintermediate.data.model.UserModel
import com.sugara.submissionandroidintermediate.databinding.ActivityHomeBinding
import com.sugara.submissionandroidintermediate.databinding.ActivitySigninBinding
import com.sugara.submissionandroidintermediate.view.ViewModelFactory
import com.sugara.submissionandroidintermediate.view.addStory.AddStoryActivity
import com.sugara.submissionandroidintermediate.view.signin.SigninActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        homeViewModel = obtainViewModel(this)

        homeViewModel.getStories()

        homeViewModel.listStories.observe(this) { listStory ->
            if(listStory.isEmpty()){
                binding.tvEmpty.visibility = View.VISIBLE
            } else {
                binding.tvEmpty.visibility = View.GONE
            }
            setListStory(listStory)
        }

        homeViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }


        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

        binding.ivLogout.setOnClickListener {
            homeViewModel.logout()
            val intent = Intent(this, SigninActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()

        }

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

    }

    private fun obtainViewModel(activity: AppCompatActivity): HomeViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(HomeViewModel::class.java)
    }

    private fun setListStory(listEvents: List<ListStoryItem>) {
        val adapter = StoryAdapter()
        adapter.submitList(listEvents)
        binding.rvStory.setHasFixedSize(true)
        binding.rvStory.isNestedScrollingEnabled = true
        binding.rvStory.adapter = adapter

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}