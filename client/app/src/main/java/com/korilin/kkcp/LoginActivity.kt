package com.korilin.kkcp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.korilin.kkcp.databinding.ActivityLoginBinding
import com.korilin.kkcp.network.LoginBody
import com.korilin.kkcp.network.httpService
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.apply {
            loginBtn.setOnClickListener {
                val email = emailInput.editText?.text.toString()
                val code = codeInput.editText?.text.toString()

                lifecycleScope.launch {
                    try {
                        val response = httpService.login(LoginBody(email, code))
                        if (response.status) {
                            println(response.data)
                        } else {
                            println(response.message)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        setContentView(binding.root)
    }
}