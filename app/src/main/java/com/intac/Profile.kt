package com.intac

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.protobuf.Empty
import com.intac.API.posts.*

import com.intac.databinding.ActivityProfileBinding

class Profile : AppCompatActivity() {
    var user_id: Long = -1
    lateinit var binding: ActivityProfileBinding
    lateinit var adapter: ProfileAdapter
    lateinit var recyclerView: RecyclerView

    var PostsThread: Thread = Thread()
    var tmpList: ArrayList<Post> = ArrayList<Post>()

    var mainPaginationLimit: Long = 6
    var firstPaginationLimit: Long = 3

    var curr_post_id: Long = 0

    private fun init() {
        recyclerView = binding.rvProfile
        adapter = ProfileAdapter();
        recyclerView.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        user_id = intent.getLongExtra("id", -1)

        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        GetUserPosts(user_id, mainPaginationLimit, curr_post_id) { it ->
            if (it.postsList.size != 0) {
                //code == 0 => OK
                if (it.postsList[0].code == 0) {
                    adapter.concatLists(makeListFromPaginationResponse(it))
                    binding.rvProfile.visibility = View.VISIBLE

                    curr_post_id = it.postsList[it.postsList.size - 1].postId.toLong()


//                    val runnable = Runnable {
//
//                        val response = GetUserPostsSync(user_id, mainPaginationLimit, curr_post_id)
//                        Log.d("TAAAAAAAAAAAAGGGGGG",response.postsList[response.postsList.size - 1].postId.toLong().toString())
//                        curr_post_id = if (response.postsList[0].code == 3) {
//                            //Значит посты закончились
//                            curr_post_id
//                        } else {
//                            response.postsList[response.postsList.size - 1].postId.toLong()
//
//                        }
//
//                        if (response.postsList[0].code != 3) {
//                            tmpList = makeListFromPaginationResponse((response))
//                        }else
//                            tmpList = ArrayList()
//
//                    }
//                    if (!PostsThread.isAlive) {
//                        PostsThread = Thread(runnable)
//                        PostsThread.start()
//
//                        adapter.concatLists(tmpList)
//                    }
                } else {
                    //TODO
                    // Server Error
                }

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
                        UpdateProfile()
                    }
                }
            }
        })
    }

    private fun UpdateProfile() {
        val runnable = Runnable {


            val response = GetUserPostsSync(user_id, mainPaginationLimit, curr_post_id)

            curr_post_id = if (response.postsList[0].code == 3) {
                curr_post_id

            } else {
                response.postsList[response.postsList.size - 1].postId.toLong()

            }
            Log.d("TAAAAAAAAAAAAGGGGGG",response.postsList[0].postId.toLong().toString())

            if (response.postsList[0].code != 3) {
                    tmpList = makeListFromPaginationResponse((response))
                }else
                    tmpList = ArrayList()
        }
        if (!PostsThread.isAlive) {
            PostsThread = Thread(runnable)
            PostsThread.start()

            adapter.concatLists(tmpList)
        }
    }

}