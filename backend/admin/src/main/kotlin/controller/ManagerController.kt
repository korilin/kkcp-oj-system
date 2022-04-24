package com.korilin.controller

import com.korilin.AdminModuleConfig
import com.korilin.IResponseBody
import com.korilin.domain.table.UserProfile
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(AdminModuleConfig.ADMIN_MANAGER_PREFIX)
class ManagerController {

    @GetMapping("/user/all")
    fun getUsers() {

    }

    @PutMapping("/user/block")
    fun blockUser() {

    }

    @GetMapping("/account/all")
    fun getAccounts() {

    }

    @PostMapping("/account/new")
    fun createAccount() {

    }

    @DeleteMapping("/account/del")
    fun deleteAccount() {

    }

    @PostMapping("/account/edit")
    fun editAccount() {

    }

    @GetMapping("/account/opts")
    fun getAdminOpts() {
        
    }
}