package com.intac.API.posts

import android.annotation.SuppressLint

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.intac.R
import com.intac.databinding.PostBinding

class PostAdapter : RecyclerView.Adapter<PostAdapter.PostHolder>() {

    private var postList = ArrayList<Post>()

    class PostHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = PostBinding.bind(view)

        fun bind(post: Post) = with(binding){
            tvPostName.text = post.title
            tvDescription.text = post.description;
            imagePost.setImageBitmap(post.photoBitmap)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.post, parent, false)
        return PostHolder(view)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.bind(postList[position])
//        holder.itemView.tvDescription.text = postList[position].description
//        holder.itemView.tvContact.text = postList[position].sellerContact
//        holder.itemView.tvPostName.text = postList[position].title
//        holder.itemView.imagePost.setImageBitmap(postList[position].photoBitmap)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addPost(post: Post) {
        postList.add(post)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun concatLists(list: ArrayList<Post>) {
        postList += list
        notifyDataSetChanged()
    }

    fun getList(): ArrayList<Post> {
        return postList
    }


}