package com.intac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.ToggleButton

class Registration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val btForMe: ToggleButton = findViewById(R.id.btForMe)
        val btForBusiness: ToggleButton = findViewById(R.id.btForBusiness)
        val plainCompanyName: EditText = findViewById(R.id.plainCompanyName)

        btForMe.setOnCheckedChangeListener { _, isChecked ->
            btForBusiness.setChecked(false)
            plainCompanyName.setVisibility(View.GONE)
        }

        btForBusiness.setOnCheckedChangeListener { _, isChecked ->
            btForMe.setChecked(false)
            plainCompanyName.setVisibility(View.VISIBLE)
        }
    }
}
