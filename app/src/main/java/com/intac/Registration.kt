package com.intac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.ToggleButton
import com.intac.databinding.ActivityFeedBinding
import com.intac.databinding.ActivityMainBinding
import com.intac.databinding.ActivityRegistrationBinding

class Registration : AppCompatActivity() {
    lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btForMe.setOnCheckedChangeListener { _, isChecked ->
            binding.btForBusiness.setChecked(false)
            binding.plainCompanyName.setVisibility(View.GONE)
        }

        binding.btForBusiness.setOnCheckedChangeListener { _, isChecked ->
            binding.btForMe.setChecked(false)
            binding.plainCompanyName.setVisibility(View.VISIBLE)
        }
    }
}
