package com.intac

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.intac.API.posts.GetFirstPostId
import com.intac.API.posts.PostAdapter
import com.intac.API.posts.getPostPaginated
import com.intac.API.posts.makeListFromPaginationResponse

import com.intac.databinding.FeedBinding

class Feed : AppCompatActivity() {

    lateinit var binding: FeedBinding
    lateinit var adapter: PostAdapter
    lateinit var recyclerView: RecyclerView

    var curr_post_id: Long = 0
    var start_post_id = curr_post_id

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btAddPost.setOnClickListener() {
            goToCreatePost()
        }

        init()

        GetFirstPostId{ it
            getPostPaginated(it.firstPostId.toLong()){
                adapter.concatLists(makeListFromPaginationResponse(it))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val visibleItemCount =
                    (recyclerView.layoutManager as LinearLayoutManager).childCount
                val pastVisibleItem =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                val total = adapter.itemCount


                if ((visibleItemCount + pastVisibleItem) >= total -5) {
                    curr_post_id += 5

                    getPostPaginated(curr_post_id){
                        adapter.concatLists(makeListFromPaginationResponse(it))
                    }


                    val checkList = adapter.getList()
                    if (checkList[checkList.size - 1].id.toLong() == checkList[0].id.toLong())

                        curr_post_id = start_post_id
                }

            }
        })
    }

    private fun goToCreatePost() {
        Log.d("TestSenderToPostCreate", "Sent to Post Creation")
        val id = intent.extras?.getLong("id")

        val intent = Intent(this@Feed, CreatePost::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    private fun init() {
        recyclerView = binding.rvPost
        adapter = PostAdapter()
        recyclerView.adapter = adapter
    }
}