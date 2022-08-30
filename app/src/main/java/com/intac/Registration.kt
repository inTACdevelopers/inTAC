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
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btForMe.setOnCheckedChangeListener { _, isChecked ->
            binding.btForBusiness.setChecked(false)
            binding.plainCompanyName.setVisibility(View.GONE)
            data_transfer()
        }

        binding.btForBusiness.setOnCheckedChangeListener { _, isChecked ->
            binding.btForMe.setChecked(false)
            binding.plainCompanyName.setVisibility(View.VISIBLE)
            data_transfer_2()
        }

        binding.btContinue.setOnClickListener(){
            forward()
        }

        binding.textEnter.setOnClickListener(){
            back()
        }

        binding.btBack.setOnClickListener(){
            back()
        }
    }

    private fun back() {
        val intent = Intent(this@Registration, MainActivity::class.java)
        startActivity(intent)
    }

    private fun forward() {
        val intent = Intent(this@Registration, Registration_page2::class.java)
        startActivity(intent)
    }


    private  fun data_transfer() {
        val intent = Intent(this@Registration, Registration_page2::class.java)

        intent.putExtra("company", binding.plainCompanyName.text.toString())
        intent.putExtra("surname", binding.plainSecondName.text.toString())
        intent.putExtra("name", binding.plainFirstName.text.toString())
        intent.putExtra("day", binding.plainDay.text.toString())
        intent.putExtra("month", binding.plainMonth.text.toString())
        intent.putExtra("year", binding.plainYear.text.toString())

        startActivity(intent)
    }

    private  fun data_transfer_2() {

    }
}
