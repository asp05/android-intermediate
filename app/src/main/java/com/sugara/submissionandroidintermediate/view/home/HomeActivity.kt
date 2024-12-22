package com.sugara.submissionandroidintermediate.view.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sugara.submissionandroidintermediate.R
import com.sugara.submissionandroidintermediate.adapter.StoryAdapter
import com.sugara.submissionandroidintermediate.databinding.ActivityHomeBinding
import com.sugara.submissionandroidintermediate.paging.LoadingState
import com.sugara.submissionandroidintermediate.view.ViewModelFactory
import com.sugara.submissionandroidintermediate.view.addStory.AddStoryActivity
import com.sugara.submissionandroidintermediate.view.maps.MapsActivity
import com.sugara.submissionandroidintermediate.view.signin.SigninActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeViewModel.getSession().observe(this) { result ->
            Log.d("HomeActivity", "onCreate session: $result")
            if (result.userId.isNullOrEmpty()) {
                val intent = Intent(this, SigninActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

        getStory()

        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

    }


    private fun getStory() {
        val adapter = StoryAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingState {
                adapter.retry()
            }
        )
        homeViewModel.story.observe(this) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_option, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ivLogout -> {
                homeViewModel.logout()
                val intent = Intent(this, SigninActivity::class.java)
                startActivity(intent)
                finish()

            }
            R.id.ivMap -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }

        }
        return super.onOptionsItemSelected(item)
    }

}