package com.intac

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.intac.API.Profile.GetUserById
import com.intac.API.Profile.GetUserPosts
import com.intac.API.Profile.GetUserPostsSync
import com.intac.API.Profile.ProfileAdapter
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

    var curr_post_id: Long = 0

    private fun init() {
        recyclerView = binding.rvProfile
        adapter = ProfileAdapter();
        recyclerView.adapter = adapter
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        user_id = intent.getLongExtra("id", -1)

        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        GetUserById(user_id) { it ->
            if (it.code == 0) {
                binding.textLogin.text = it.login
                binding.textusername.text = it.name + " " + it.surname
                binding.numPost.text = it.countOfPosts.toString()
            } else {
                //TODO
                // Ошибка сервера
            }

        }

        GetUserPosts(user_id, mainPaginationLimit, curr_post_id) { it ->
            if (it.postsList.size != 0) {
                //code == 0 => OK
                if (it.postsList[0].code == 0) {
                    adapter.concatLists(makeListFromPaginationResponse(it))
                    binding.rvProfile.visibility = View.VISIBLE

                    curr_post_id = it.postsList[it.postsList.size - 1].postId.toLong()


                } else {
                    //TODO
                    // Server Error
                }

            }
        }

        binding.btBackEditProfile.setOnClickListener() {
            appBack()
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

            if (response.postsList[0].code != 3) {
                tmpList = makeListFromPaginationResponse((response))
            } else
                tmpList = ArrayList()
        }

        if (!PostsThread.isAlive) {
            PostsThread = Thread(runnable)
            PostsThread.start()

            adapter.concatLists(tmpList)
        }
    }

    private fun appBack() {
        val intent = Intent(this@Profile, Feed::class.java)
        startActivity(intent)
    }

}