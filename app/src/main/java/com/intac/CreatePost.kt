package com.intac

import android.R.attr.bitmap
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.intac.API.posts.*
import com.intac.databinding.ActivityCreatePostBinding
import java.lang.Exception


@Suppress("DEPRECATION")
class CreatePost : AppCompatActivity() {
    lateinit var binding: ActivityCreatePostBinding
    lateinit var choosedPhotoBitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btBackCreatePost.setOnClickListener() {
            appBack()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 10 && data != null && data.data != null) {
            if (resultCode == RESULT_OK) {
                choosedPhotoBitmap =
                    MediaStore.Images.Media.getBitmap(this.contentResolver, data.data)
            } else {
                Log.d("TestPhoto", resultCode.toString()) // временно
            }

        }
    }

    fun choosePhoto(view: View) {
        val chooser: Intent = Intent()
        chooser.type = "image/*"
        chooser.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(chooser, 10)
    }

    fun UploadPost(view: View) {
        try {
            val PostName = binding.plainPostName.text.toString()
            val PostDescription = binding.plainPostDescription.text.toString()
            val PostContacts = binding.plainContacts.text.toString()
            val id = intent.extras?.getLong("id") as Long

            if (PostName != "" && PostDescription != "" && PostContacts != "") {
                val check: Int = choosedPhotoBitmap.width

                val post: Post = Post(
                    title = PostName,
                    description = PostDescription,
                    sellerContact = PostContacts,
                    photoBitmap = choosedPhotoBitmap,
                    from_user = id
                )

                val makePostResponse = makePost(post)

                if (makePostResponse.state == "OK") {
                    val intent = Intent(this@CreatePost, Feed::class.java)
                    intent.putExtra("id", id)
                    startActivity(intent)
                } else {
                    Log.d("TestUploadPost_2", makePostResponse.state) // временно
                }
            } else {
                Log.d("TestUploadPost", "Smth not named") // временно
            }
        } catch (w: Exception) {
            Log.d("TestUploadPost", "Photo not chosen")
        }
    }

    private fun appBack() {
        val id = intent.extras?.getInt("id")

        val intent = Intent(this@CreatePost, Feed::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }
}