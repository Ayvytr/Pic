package com.ayvytr.common

import androidx.annotation.Keep
import com.tencent.mmkv.MMKV

/**
 * @author Administrator
 */
@Keep
object MmkvManager {
    const val ID_USERINFO = "id_userinfo"
    const val ID_APP_CONFIG = "id_app_config"

    @JvmField
    val mmkvUser = MMKV.mmkvWithID(ID_USERINFO)
    @JvmField
    val mmkvAppConfig = MMKV.mmkvWithID(ID_APP_CONFIG)

    private const val KEY_IS_FIRST_ENTER_APP = "key_is_first_enter_app"

    fun isFirstEnterApp(): Boolean {
        return mmkvAppConfig.getBoolean(KEY_IS_FIRST_ENTER_APP, true)
    }

    fun setFirstEnteredApp() {
        mmkvAppConfig.putBoolean(KEY_IS_FIRST_ENTER_APP, false)
    }
}