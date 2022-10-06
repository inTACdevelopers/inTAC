package com.intac

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import com.intac.API.posts.LikePost
import com.intac.API.users.*

import com.intac.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btEnter.setOnClickListener() {
            appSignUp()
        }

        binding.textReg.setOnClickListener() {
            goToReg()
        }
    }

    private fun appSignUp() { // Вход в аккаунт

        val SignUpResponse =
            SingUp(binding.plainLogin.text.toString(), binding.plainPass.text.toString())

        if (SignUpResponse.state == "OK") {
            Log.d("LoginTest", "Success")

            CreateSession(SignUpResponse.id) {
                if (it.state == "OK") {
                    val intent = Intent(this@MainActivity, Feed::class.java)
                    intent.putExtra("id", SignUpResponse.id.toLong())
                    intent.putExtra("session_name", it.sessionName)
                    startActivity(intent)
                } else {
                    //TODO
                    // Здесь обработать ошибку, у пользователя какого-то хуя
                    // сохранилась сессия (она должна удаляться)

                }
            }

        } else {
            Log.d(
                "LoginTest",
                SignUpResponse.state
            ) // в будущем будет соответствующая ошибка на экране
        }
    }

    private fun goToReg() {
        Log.d("TestSenderToReg", "Sent to reg")

        val intent = Intent(this@MainActivity, Registration::class.java)
        startActivity(intent)
    }


}