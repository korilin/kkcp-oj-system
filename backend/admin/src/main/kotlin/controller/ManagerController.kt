package com.korilin.controller

import com.korilin.AdminModuleConfig
import com.korilin.IResponseBody
import com.korilin.domain.table.AdminAccount
import com.korilin.domain.table.AdminOptionRecord
import com.korilin.domain.table.UserProfile
import com.korilin.service.ManagerService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(AdminModuleConfig.ADMIN_MANAGER_PREFIX)
class ManagerController(private val service: ManagerService) {

    @GetMapping("/user/all")
    suspend fun getUsers(): IResponseBody<Array<UserProfile>> {
        val users = service.allUser().toTypedArray()
        return IResponseBody.success(data = users)
    }

    @PutMapping("/user/block")
    suspend fun blockUser(userId: Int, status: Boolean): IResponseBody<Boolean> {
        val r = service.blockUser(userId, status)
        return IResponseBody.success(r)
    }

    @GetMapping("/account/all")
    suspend fun getAccounts(): IResponseBody<Array<AdminAccount>> {
        val accounts = service.allAccount().toTypedArray()
        return IResponseBody.success(data = accounts)
    }

    @PostMapping("/account/new")
    suspend fun createAccount(@RequestBody account: AdminAccount): IResponseBody<Boolean> {
        val r = service.addAccount(account)
        return IResponseBody.success(r)
    }

    @DeleteMapping("/account/del")
    suspend fun deleteAccount(email: String): IResponseBody<Boolean> {
        val r = service.deleteAccount(email)
        return IResponseBody.success(r)
    }

    @GetMapping("/account/opts")
    suspend fun getAdminOpts(): IResponseBody<Array<AdminOptionRecord>> {
        val opts = service.allOpts().toTypedArray()
        return IResponseBody.success(opts)
    }
}