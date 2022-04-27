package com.korilin.kkcp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korilin.kkcp.network.Account
import com.korilin.kkcp.network.httpService
import com.korilin.kkcp.network.request
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AccountViewModel : ViewModel() {

    val accounts: MutableList<Account> = mutableListOf()

    var onAccountsChange: (Int, Int) -> Unit = { _, _ -> }

    fun initAccounts() {
        viewModelScope.launch {
            request(call = {
                httpService.queryAllAccount()
            }) {
                accounts.addAll(it!!)
                onAccountsChange(0, it.size)
            }
        }
    }

    fun deleteAccount(position: Int, block: () -> Unit) {
        val email = accounts[position].email
        viewModelScope.launch {
            request(call = {
                httpService.deleteAccount(email = email)
            }) {
                if (it == true) {
                    accounts.removeAt(position)
                    block()
                }
            }
        }
    }

    fun newAccount(email: String, name: String, level: Int) {
        val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val time = LocalDateTime.now().format(pattern)
        val account = Account(email, name, level, time)
        viewModelScope.launch {
            request(call = {
                httpService.newAccount(body = account)
            }) {
                if (it == true) {
                    accounts.add(account)
                    onAccountsChange(accounts.size - 1, 1)
                }
            }
        }
    }
}