package com.intac

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.intac.API.posts.*

import com.intac.databinding.FeedBinding

class Feed : AppCompatActivity() {

    lateinit var binding: FeedBinding
    lateinit var adapter: PostAdapter
    lateinit var recyclerView: RecyclerView

    var curr_post_id: Long = 0
    var start_post_id = curr_post_id

    var PostsThread: Thread = Thread()
    var tmpList: ArrayList<Post> = ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btAddPost.setOnClickListener() {
            goToCreatePost()
        }

        init()

        GetFirstPostId { it ->
            start_post_id = it.firstPostId.toLong()
            curr_post_id = it.firstPostId.toLong()

            getPostPaginated(it.firstPostId.toLong()) {
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
                val totalItemCount = (recyclerView.layoutManager as LinearLayoutManager).itemCount
                val firstVisibleItems =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                if (visibleItemCount + firstVisibleItems >= totalItemCount - 1) {

                    val runnable = Runnable {

                        curr_post_id += 5

                        val checklist = adapter.getList()
                        if (checklist[checklist.size - 1].id == checklist[0].id) {
                            curr_post_id = start_post_id + 1
                        }

                        val response = getPostPaginatedSync(curr_post_id)

                        tmpList = makeListFromPaginationResponse((response))

                    }

                    if (!PostsThread.isAlive) {
                        PostsThread = Thread(runnable)
                        PostsThread.start()

                        adapter.concatLists(tmpList)
                    }
                }
            }
        })
    }

    private fun goToCreatePost() {
        Log.d("TestSenderToPostCreate", "Sent to Post Creation")
        val id = intent.extras?.getInt("id")

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