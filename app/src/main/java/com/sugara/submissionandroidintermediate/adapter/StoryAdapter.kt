package com.sugara.submissionandroidintermediate.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sugara.submissionandroidintermediate.data.model.ListStoryItem
import com.sugara.submissionandroidintermediate.databinding.ItemStoryBinding
import com.sugara.submissionandroidintermediate.view.home.HomeDetailActivity


class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    inner class MyViewHolder(internal val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (story : ListStoryItem){
            Glide.with(binding.root.context)
                .load(story.photoUrl)
                .into(binding.ivPicture)
            binding.tvTitle.text = story.name
            binding.tvContent.text = story.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryAdapter.MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryAdapter.MyViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story!!)

        holder.itemView.setOnClickListener {
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(holder.binding.ivPicture, "image"),
                    Pair(holder.binding.tvTitle, "title"),
                    Pair(holder.binding.tvContent, "content")
                )
            val moveDetailStory = Intent(holder.itemView.context, HomeDetailActivity::class.java)
            moveDetailStory.putExtra(HomeDetailActivity.EXTRA_ID, story.id)
            holder.itemView.context.startActivity(moveDetailStory, optionsCompat.toBundle())
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>(){
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}