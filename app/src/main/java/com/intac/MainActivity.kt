package com.intac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.intac.users.*
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onResume() {
        super.onResume()

//        val user: User = User(
//            name = "alex",
//            surname = "mitroshkin",
//            login = "89020930888",
//            pass = "pass",
//            company = "INTAC"
//        )
//
//        val r = SingUp("89020930888","passs")
//        Log.d("Test", r.state)
//        Log.d("Test", "123")
    }
}