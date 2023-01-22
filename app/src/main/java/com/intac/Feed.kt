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
import com.intac.API.users.CreateSession
import com.intac.API.users.DropSessionSync
import com.intac.databinding.FeedBinding


@Suppress("DEPRECATION")
class Feed : AppCompatActivity() {

    lateinit var binding: FeedBinding
    lateinit var adapter: PostAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var session_name: String
    var user_id: Long = -1

    var curr_post_id: Long = 0
    var start_post_id: Long = 0

    var PostsThread: Thread = Thread()
    var tmpList: ArrayList<Post> = ArrayList<Post>()

    var mainPaginationLimit: Long = 3
    var firstPaginationLimit: Long = 3


    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        user_id = intent.getLongExtra("id", -1)


        super.onCreate(savedInstanceState)
        binding = FeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            val color = Color.parseColor("#0043BE");
            pbLoader.indeterminateDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
            navView.setOnNavigationItemSelectedListener()
            {
                when (it.itemId) {
                    R.id.navigation_feed -> {}
                    R.id.navigation_search -> {
                        val intent = Intent(this@Feed, Search::class.java)
                        intent.putExtra("id", user_id)
                        startActivity(intent)
                    }
                    R.id.navigation_addPost -> {
                        val intent = Intent(this@Feed, CreatePost::class.java)
                        intent.putExtra("id", user_id)
                        startActivity(intent)
                    }
                    R.id.navigation_reactions -> {
                        val intent = Intent(this@Feed, Reactions::class.java)
                        intent.putExtra("id", user_id)
                        startActivity(intent)
                    }
                    R.id.navigation_profile -> {
                        val intent = Intent(this@Feed, Profile::class.java)
                        intent.putExtra("id", user_id)
                        startActivity(intent)
                    }
                }
                true
            }

            rvPost.visibility = View.GONE
        }

        init()

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

    override fun onStop() {
        DropSessionSync(session_name)
        super.onStop()
    }

    override fun onStart() {
        super.onStart()

        CreateSession(user_id.toInt()) {
            session_name = it.sessionName


            if (it.code == 0) {
                getPostPaginated(curr_post_id, firstPaginationLimit, session_name,user_id) { it ->
                    if(it.postsList != null && it.postsList[0].code==0) {


                        adapter.concatLists(makeListFromPaginationResponse(it))

                        binding.rvPost.visibility = View.VISIBLE
                        binding.pbLoader.visibility = View.GONE

                        curr_post_id += mainPaginationLimit


                        val runnable = Runnable {

                            val response =
                                getPostPaginatedSync(
                                    curr_post_id,
                                    mainPaginationLimit,
                                    session_name,
                                    user_id
                                )

                            curr_post_id = if (response.postsList[0].code == 3) {
                                start_post_id

                            } else {
                                curr_post_id + mainPaginationLimit

                            }

                            if (response.postsList[0].code != 3)
                                tmpList = makeListFromPaginationResponse((response))
                            else
                                tmpList = ArrayList()
                        }
                        if (!PostsThread.isAlive) {
                            PostsThread = Thread(runnable)
                            PostsThread.start()

                            adapter.concatLists(tmpList)
                        }

                    }
                }
            } else {
                //TODO
                //Обработать ошибку сервера
            }

        }
    }

    private fun init() {
        recyclerView = binding.rvPost
        adapter = PostAdapter(user_id);
        recyclerView.adapter = adapter
    }

    private fun UpdateFeed() {
        val runnable = Runnable {

            val response = getPostPaginatedSync(curr_post_id, mainPaginationLimit, session_name,user_id)

            curr_post_id = if (response.postsList[0].code == 3) {
                start_post_id

            } else {
                curr_post_id + mainPaginationLimit

            }
            if (response.postsList[0].code != 3)
                tmpList = makeListFromPaginationResponse((response))
            else
                tmpList = ArrayList()
        }
        if (!PostsThread.isAlive) {
            PostsThread = Thread(runnable)
            PostsThread.start()

            adapter.concatLists(tmpList)
        }
    }
}