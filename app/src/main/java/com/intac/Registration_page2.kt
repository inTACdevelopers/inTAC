package com.intac

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.intac.databinding.ActivityRegistrationPage2Binding
import com.intac.API.users.*

class Registration_page2 : AppCompatActivity() {
    lateinit var binding: ActivityRegistrationPage2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationPage2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btBackPage2.setOnClickListener(){
            appBack()
        }

        binding.btCreateAcc.setOnClickListener(){
            appSignIn()
        }
    }

    private fun appBack() {
        // в MVP введенные данные на прошлой странице будут сохраняться, если вернуться назад с этой

        val intent = Intent(this@Registration_page2, Registration::class.java)
        startActivity(intent)
    }

    private fun appSignIn() {
        if (binding.plainRegPass.text.toString() == binding.plainRepeatPass.text.toString()) {
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

            val SignInResponse = SingIn(user)

            if (SignInResponse.state == "OK") {
                Log.d("TestReg", "Success")

                val SignUpResponse = SingUp(user.login, user.pass)
                println(SignUpResponse.state)
                val intent = Intent(this@Registration_page2, Feed::class.java)
                intent.putExtra("id", SignUpResponse.id.toLong())
                startActivity(intent)
            } else {
                Log.d("TestReg", SignInResponse.state) // в будущем будет соответствующая ошибка на экране
            }
        } else {
            Log.d("TestReg", "The passwords are not the same") // в будущем будет соответствующая ошибка на экране
        }
    }
}