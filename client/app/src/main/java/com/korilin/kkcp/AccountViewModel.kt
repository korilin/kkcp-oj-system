package com.korilin.kkcp

import androidx.lifecycle.ViewModel
import com.korilin.kkcp.network.Account

class AccountViewModel : ViewModel() {

    val accounts: MutableList<Account> = mutableListOf()
}