package com.ayvytr.pic.bean

import android.os.Parcelable
import android.text.format.DateUtils
import com.ayvytr.common.bean.Storage
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat


/**
 * @author Administrator
 */
@Parcelize
data class Photo(
    val name: String,
    //父目录
    val parent: String,
    //全路径
    val path: String,
    val size: Storage,
    val width: Int,
    val height: Int,
    val createDate: Long,
    val modifiedDate: Long
):
    Parcelable {

    fun modifiedDateStr(): String {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:s").format(modifiedDate)
    }
}