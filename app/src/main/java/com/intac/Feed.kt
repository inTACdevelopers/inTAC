package com.intac

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.intac.databinding.ActivityFeedBinding
import com.intac.databinding.ActivityMainBinding

class Feed : AppCompatActivity() {
    lateinit var binding: ActivityFeedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btAddPost.setOnClickListener() {
            goToCreatePost()
        }
    }

    private fun goToCreatePost() {
        Log.d("TestSenderToPostCreate", "Sent to Post Creation")
        val id = intent.extras?.getInt("id")

        val intent = Intent(this@Feed, CreatePost::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }
}