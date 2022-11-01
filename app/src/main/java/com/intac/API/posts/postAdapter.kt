package com.intac.API.posts

import android.annotation.SuppressLint

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.intac.R
import kotlinx.android.synthetic.main.post.view.*

class PostAdapter : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    private var postList = ArrayList<Post>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tvDescription.text = postList[position].description
        //holder.itemView.tvContact.text = postList[position].sellerContact
        holder.itemView.tvPostName.text = postList[position].title
        holder.itemView.imagePost.setImageBitmap(postList[position].photoBitmap)
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