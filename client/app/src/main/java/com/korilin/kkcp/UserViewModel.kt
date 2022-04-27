package com.korilin.kkcp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korilin.kkcp.network.User
import com.korilin.kkcp.network.httpService
import com.korilin.kkcp.network.request
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    val users: MutableList<User> = mutableListOf()

    var onUsersInsert: (Int, Int) -> Unit = { _, _-> }
    var onUsersUpdate: (Int) -> Unit = { _ -> }

    fun initUsers() {
        viewModelScope.launch {
            request(call = {
                httpService.queryAllUser()
            }) {
                users.addAll(it!!)
                onUsersInsert(0, it.size)
            }
        }
    }

    fun updateUserState(position: Int, status: Boolean) {
        val uid = users[position].id
        viewModelScope.launch {
            request(call = {
                httpService.blockUser(userId = uid, status = status)
            }) {
                users[position].block = status
                onUsersUpdate(position)
            }
        }
    }
}