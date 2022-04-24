package com.korilin.kkcp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korilin.kkcp.network.Account
import com.korilin.kkcp.network.httpService
import com.korilin.kkcp.network.request
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

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
}