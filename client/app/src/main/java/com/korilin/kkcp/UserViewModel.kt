package com.korilin.kkcp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korilin.kkcp.network.Account
import com.korilin.kkcp.network.User
import com.korilin.kkcp.network.httpService
import com.korilin.kkcp.network.request
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    val users: MutableList<User> = mutableListOf()

    var onUsersChange: (Int, Int) -> Unit = { _, _ -> }

    fun initAccounts() {
        viewModelScope.launch {
            request(call = {
                httpService.queryAllUser()
            }) {
                users.addAll(it!!)
                onUsersChange(0, it.size)
            }
        }
    }
}