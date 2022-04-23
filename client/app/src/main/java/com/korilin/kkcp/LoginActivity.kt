package com.korilin.kkcp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.korilin.kkcp.databinding.ActivityLoginBinding
import com.korilin.kkcp.network.LoginBody
import com.korilin.kkcp.network.httpService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.apply {
            codeSendBtn.setOnClickListener { _ ->
                codeSendBtn.isEnabled = false
                lifecycleScope.launch {
                    try {
                        val response = httpService.sendCode(emailInput.editText?.text.toString())
                        if (response.status) {
                            repeat(60) {
                                codeSendBtn.text = "${60 - it}"
                                delay(1000)
                            }
                        } else {
                            showToast(response.message)
                        }
                        codeSendBtn.isEnabled = true
                    } catch (e: Exception) {
                        e.printStackTrace()
                        codeSendBtn.isEnabled = true
                    }
                }
            }
            loginBtn.setOnClickListener {
                val email = emailInput.editText?.text.toString()
                val code = codeInput.editText?.text.toString()

                lifecycleScope.launch {
                    try {
                        val response = httpService.login(LoginBody(email, code))
                        if (response.status) {
                            showToast("SUCCESS")
                        } else {
                            showToast(response.message)
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