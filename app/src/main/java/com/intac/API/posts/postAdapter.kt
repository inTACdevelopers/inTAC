package com.intac.API.posts

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.intac.API.users.User
import com.intac.R
import com.intac.databinding.PostBinding

class PostAdapter(var user_id: Long) : RecyclerView.Adapter<PostAdapter.PostHolder>() {

    private var postList = ArrayList<Post>()

    class PostHolder(view: View, var user_id: Long) : RecyclerView.ViewHolder(view) {
        val binding = PostBinding.bind(view)

        fun bind(post: Post) = with(binding) {
            tvPostName.text = post.title
            tvDescription.text = post.description;
            imagePost.setImageBitmap(post.photoBitmap)

            btBuying.setOnClickListener {
                /*var url = post.sellerContact

                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://" + url
                }

                val browserintent = Intent(Intent.ACTION_VIEW)
                browserintent.setData(Uri.parse(url))
                startActivity(browserintent)*/
            }

            if(post.like){
                btLike.isChecked = true
                post.was_like = 1
            }

            btLike.setOnClickListener {
                if (post.was_like == 0) {
                    post.Like(user_id) {
                        post.was_like = 1
                    }
                } else {
                    post.UnLike(user_id){
                        post.was_like = 0
                    }

                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post, parent, false)
        return PostHolder(view, user_id)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
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