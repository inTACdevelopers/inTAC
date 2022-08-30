package com.intac

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.intac.API.users.*

import com.intac.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btEnter.setOnClickListener() {
            appSignUp()
        }

        binding.textReg.setOnClickListener() {
            Log.d("TestSenderToReg", "Sent to reg")

            val intent = Intent(this@MainActivity, Registration::class.java)
            startActivity(intent)
        }

        val user: User = User(
            name = "Ivan",
            surname = "Tokarev",
            login = "12345",
            pass = "123",
            company = "smth",
            birth = "10.2.2"
        )

        val d = SingIn(user)
        Log.d("RegTest", d.state)
    }

    private fun appSignUp() { // Вход в аккаунт
        val sign = SingUp(binding.plainLogin.text.toString(), binding.plainPass.text.toString())

        if (sign.state == "OK") {
            Log.d("LoginTest", "Success")

            val intent = Intent(this@MainActivity, Feed::class.java)
            startActivity(intent)
        } else {
            Log.d("LoginTest", sign.state)
        }
    }
}