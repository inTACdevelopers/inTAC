package com.intac.API.Profile

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.intac.API.posts.Post
import com.intac.R
import com.intac.databinding.ProfilePostBinding

class ProfileAdapter() : RecyclerView.Adapter<ProfileAdapter.ProfilePostHolder>() {
    private var postList = ArrayList<Post>()


    class ProfilePostHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ProfilePostBinding.bind(view)

        fun bind(post: Post) = with(binding) {
            imagePost.setImageBitmap(post.photoBitmap)

            imagePost.setOnClickListener{

                //TODO
                // Добавь сюда действие по переходу на ленту (вертикальную, а не горизонтальную)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilePostHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.profile_post, parent, false)
        return ProfilePostHolder(view)
    }

    override fun onBindViewHolder(holder: ProfilePostHolder, position: Int) {
        holder.bind(postList[position])
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun concatLists(list: ArrayList<Post>) {
        postList += list
        notifyDataSetChanged()
    }

}