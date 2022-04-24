package com.korilin.kkcp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.korilin.kkcp.databinding.ActivityLoginBinding
import com.korilin.kkcp.network.LoginBody
import com.korilin.kkcp.network.httpService
import com.korilin.kkcp.storage.Store
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
                            showToast("Already Send")
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
                            Store.token = response.data!!.token
                            Store.account = response.data.account
                            showToast("Login Success")
                            startActivity(MainActivity::class.java)
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