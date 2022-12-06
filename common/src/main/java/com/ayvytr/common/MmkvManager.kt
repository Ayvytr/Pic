package com.ayvytr.common

import androidx.annotation.Keep
import com.ayvytr.common.bean.PreviewType
import com.ayvytr.common.bean.SortType
import com.tencent.mmkv.MMKV

/**
 * @author Administrator
 */
@Keep
object MmkvManager {
    const val ID_USERINFO = "id_userinfo"
    const val ID_APP = "id_app"

    const val KEY_SORT_TYPE = "key_sort_type"
    const val KEY_PREVIEW_BY = "key_preview_by"

    @JvmField
    val mmkvUser = MMKV.mmkvWithID(ID_USERINFO)

    @JvmField
    val mmkvApp = MMKV.mmkvWithID(ID_APP)

    private const val KEY_IS_FIRST_ENTER_APP = "key_is_first_enter_app"

    fun isFirstEnterApp(): Boolean {
        return mmkvApp.getBoolean(KEY_IS_FIRST_ENTER_APP, true)
    }

    fun setFirstEnteredApp() {
        mmkvApp.putBoolean(KEY_IS_FIRST_ENTER_APP, false)
    }

    fun setSortType(sortType: SortType) {
        mmkvApp.encode(KEY_SORT_TYPE, sortType)
    }

    fun getSortType(): SortType {
        return mmkvApp.decodeParcelable(KEY_SORT_TYPE, SortType::class.java, SortType.NAME)!!
    }

    fun setPreviewType(previewType: PreviewType) {
        mmkvApp.encode(KEY_PREVIEW_BY, previewType)
    }

    fun getPreviewType(): PreviewType {
        return mmkvApp.decodeParcelable(
            KEY_PREVIEW_BY, PreviewType::class.java, PreviewType.IMAGE
        )!!
    }
}