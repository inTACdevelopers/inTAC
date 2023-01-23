package com.intac

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.ToggleButton
import com.intac.databinding.ActivityMainBinding
import com.intac.databinding.ActivityRegistrationBinding

class Registration : AppCompatActivity() {
    lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btForMe.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.btForBusiness.setChecked(false)

                binding.btForMe.isClickable = false
                binding.btForBusiness.isClickable = true

                binding.plainCompanyName.setVisibility(View.GONE)
            }
        }

        binding.btForBusiness.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.btForMe.setChecked(false)

                binding.btForBusiness.isClickable = false
                binding.btForMe.isClickable = true

                binding.plainCompanyName.setVisibility(View.VISIBLE)
            }
        }

        binding.btContinue.setOnClickListener(){
            appContinueReg()
        }

        binding.btBack.setOnClickListener(){
            appBack()
        }
    }

    private fun appBack() {
        val intent = Intent(this@Registration, MainActivity::class.java)
        startActivity(intent)
    }

    private fun appContinueReg() {
        val intent = Intent(this@Registration, Registration_page2::class.java)

        intent.putExtra("company", binding.plainCompanyName.text.toString())
        intent.putExtra("surname", binding.plainSecondName.text.toString())
        intent.putExtra("name", binding.plainFirstName.text.toString())
        intent.putExtra("day", binding.plainDay.text.toString())
        intent.putExtra("month", binding.plainMonth.text.toString())
        intent.putExtra("year", binding.plainYear.text.toString())

        startActivity(intent)
    }

    // тут надо написать обработчик нажатия для btForMe и btForBusiness
}
