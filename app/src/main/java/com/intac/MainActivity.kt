package com.intac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.intac.databinding.ActivityMainBinding
import com.intac.users.*
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btEnter.setOnClickListener(){
            appSignIn()
        }

        binding.textReg.setOnClickListener(){
            // перейти на слайд регистрации
        }

        val user: User = User(
            name = "Ivan",
            surname = "Tokarev",
            login = "123",
            pass = "123",
            company = "Dildo.corp"
        )

        val d = SingIn(user)
        Log.d("RegTest", d.state)
    }

    private fun appSignIn(){ // Вход в аккаунт
        var sign = SingUp(binding.plainLogin.text.toString(), binding.plainPass.text.toString())

        Log.d("LoginTest", binding.plainLogin.text.toString())
        Log.d("LoginTest", binding.plainPass.text.toString())

        if (sign.state == "OK") {
            //тут надо перевести на экран ленты, но пока тест
            Log.d("LoginTest", "Success")
        } else {
            Log.d("LoginTest", sign.state)
        }
    }
}