package com.korilin.kkcp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.textfield.TextInputEditText
import com.korilin.kkcp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.apply {
            loginBtn.setOnClickListener {
                val email = emailInput.editText?.text.toString()
                val code = codeInput.editText?.text.toString()
                
            }
        }
        setContentView(binding.root)
    }
}