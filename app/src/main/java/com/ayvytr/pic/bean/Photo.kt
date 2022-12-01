package com.ayvytr.pic.bean

import android.os.Parcelable
import com.ayvytr.common.bean.Storage
import kotlinx.parcelize.Parcelize


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
    val height: Int
):
    Parcelable {
}