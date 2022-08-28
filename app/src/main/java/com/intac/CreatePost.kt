package com.intac

import android.R.attr.bitmap
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.intac.databinding.ActivityCreatePostBinding


@Suppress("DEPRECATION")
class CreatePost : AppCompatActivity() {
    lateinit var binding: ActivityCreatePostBinding
    lateinit var choosedPhotoBitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && data != null && data.data != null) {
            if (resultCode == RESULT_OK) {
                choosedPhotoBitmap =
                    MediaStore.Images.Media.getBitmap(this.contentResolver, data.data)
            } else {
                // Ваня, обработай тут ошибку, если фото по какой-то причине не выбрано, или это не фото
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
            val check: Int = choosedPhotoBitmap.width

            // Здесь создаешь объект класса Post и передаёшь его в makePost(Post)
            // *тебе нужено вытащить id пользователя (который сейчас авторизирован)


        } catch (w: Exception) {
           // Фотка не выбрана
        }
    }
}