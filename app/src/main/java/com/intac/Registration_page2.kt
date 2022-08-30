package com.intac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.intac.databinding.ActivityRegistrationPage2Binding

class Registration_page2 : AppCompatActivity() {
    lateinit var binding: ActivityRegistrationPage2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationPage2Binding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}