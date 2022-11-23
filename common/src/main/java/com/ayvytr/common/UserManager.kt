package com.ayvytr.common

import androidx.annotation.Keep

/**
 * Created by Richard on 2018/7/31
 * 用户信息管理类
 */
@Keep
object UserManager {
    private val mmkvUser = MmkvManager.mmkvUser
    fun clearUserInfo() {
        mmkvUser.clearAll()
    }
}