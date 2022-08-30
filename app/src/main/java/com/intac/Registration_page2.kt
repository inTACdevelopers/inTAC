package com.intac

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.intac.databinding.ActivityMainBinding
import com.intac.databinding.ActivityRegistrationBinding
import com.intac.databinding.ActivityRegistrationPage2Binding
import com.intac.API.users.SingIn
import com.intac.API.users.User

class Registration_page2 : AppCompatActivity() {
    lateinit var binding: ActivityRegistrationPage2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationPage2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btBackPage2.setOnClickListener(){
            back()
        }

        binding.btCreateAcc.setOnClickListener(){
            appSignUp()
            forward()
        }
    }

    private fun back() {
        val intent = Intent(this@Registration_page2, Registration::class.java)
        startActivity(intent)
    }

    private fun forward() {
        val intent = Intent(this@Registration_page2, Feed::class.java)
        startActivity(intent)
    }

    private fun appSignUp() {
        val user: User = User(
            name = intent.getStringExtra("name") as String,
            surname = intent.getStringExtra("surname") as String,
            login = binding.plainRegLogin.text.toString(),
            pass = binding.plainRegPass.text.toString(),
            company = intent.getStringExtra("company") as String,
            birth = intent.getStringExtra("day") as String +
                    intent.getStringExtra("month") as String +
                    intent.getStringExtra("year") as String
        )
        val response = SingIn(user)
        // проверить правильность ввода пароля
        if (response.state == "OK") {
            Log.d("RegTest", "Success")

            forward()
        } else {
            Log.d("RegTest", response.state)
        }
    }
}