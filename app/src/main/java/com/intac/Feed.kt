package com.intac

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.intac.API.posts.*
import com.intac.databinding.FeedBinding
import kotlin.math.log


@Suppress("DEPRECATION")
class Feed : AppCompatActivity() {

    lateinit var binding: FeedBinding
    lateinit var adapter: PostAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var session_name: String

    var curr_post_weight: Double = 0.0
    var start_post_weight: Double = 0.0

    var PostsThread: Thread = Thread()
    var tmpList: ArrayList<Post> = ArrayList<Post>()

    var mainPaginationLimit: Long = 3
    var firstPaginationLimit: Long = 3


    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()

        session_name = intent.getStringExtra("session_name") as String

        super.onCreate(savedInstanceState)
        binding = FeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvPost.visibility = View.GONE


        with(binding) {
            val color = Color.parseColor("#0043BE");
            pbLoader.indeterminateDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }

        init()

        binding.btAddPost.setOnClickListener() {
            goToCreatePost()
        }

        GetFirstPostId(session_name) { it ->

            if (it.code != 1) {
                curr_post_weight = it.weight
                start_post_weight = it.weight

                getPostPaginated(it.weight, firstPaginationLimit,session_name) {
                    adapter.concatLists(makeListFromPaginationResponse(it))

                    binding.rvPost.visibility = View.VISIBLE
                    binding.pbLoader.visibility = View.GONE

                    curr_post_weight = it.postsList[it.postsList.size - 1].weight


                    val runnable = Runnable {

                        val paginate: Long = 2

                        val response = getPostPaginatedSync(curr_post_weight, paginate,session_name)

                        curr_post_weight = if(response.postsList[0].code == 3) {
                            start_post_weight

                        } else {
                            response.postsList[response.postsList.size - 1].weight

                        }

                        tmpList = makeListFromPaginationResponse((response))
                    }
                    if (!PostsThread.isAlive) {
                        PostsThread = Thread(runnable)
                        PostsThread.start()

                        adapter.concatLists(tmpList)
                    }


                }
            } else {
                //TODO
                //Обработать ошибку сервера
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



                if (visibleItemCount + firstVisibleItems >= totalItemCount - 10) {
                    if (totalItemCount != 0) {
                        UpdateFeed()
                    }

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

    private fun UpdateFeed() {
        val runnable = Runnable {



            val response = getPostPaginatedSync(curr_post_weight, mainPaginationLimit,session_name)

            curr_post_weight = if(response.postsList[0].code == 3) {
                start_post_weight

            } else {
                response.postsList[response.postsList.size - 1].weight

            }

            tmpList = makeListFromPaginationResponse((response))
        }
        if (!PostsThread.isAlive) {
            PostsThread = Thread(runnable)
            PostsThread.start()

            adapter.concatLists(tmpList)
        }
    }
}