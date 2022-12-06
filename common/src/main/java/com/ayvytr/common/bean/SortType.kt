package com.ayvytr.common.bean

import android.os.Parcelable
import androidx.annotation.StringRes
import com.ayvytr.common.R
import com.ayvytr.ktx.provider.ContextProvider
import kotlinx.parcelize.Parcelize

/**
 * @author Administrator
 */
@Parcelize
enum class SortType(@StringRes val stringId: Int): Parcelable {
    NAME(R.string.name),
    TIME_DESC(R.string.time_desc),
    TIME(R.string.time);

    companion object {
        val items by lazy {
            values().map {
                ContextProvider.getContext().getString(it.stringId)
            }.toTypedArray()
        }

        fun valueOf(i: Int): SortType {
            return when (i) {
                1 -> TIME_DESC
                2 -> TIME
                else -> NAME
            }
        }
    }
}