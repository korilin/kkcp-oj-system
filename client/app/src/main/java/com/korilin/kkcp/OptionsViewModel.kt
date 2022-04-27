package com.korilin.kkcp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.korilin.kkcp.network.Option
import com.korilin.kkcp.network.httpService
import com.korilin.kkcp.network.request
import kotlinx.coroutines.launch

class OptionsViewModel : ViewModel() {
    val options: MutableList<Option> = mutableListOf()

    var onUsersInsert: (Int, Int) -> Unit = { _, _ -> }

    fun initOptions() {
        viewModelScope.launch {
            request(call = {
                httpService.queryAllOption()
            }) {
                options.addAll(it!!)
                onUsersInsert(0, it.size)
            }
        }
    }
}