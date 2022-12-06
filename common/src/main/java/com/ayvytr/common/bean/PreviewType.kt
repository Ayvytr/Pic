package com.ayvytr.common.bean

import android.os.Parcelable
import com.ayvytr.common.R
import com.ayvytr.ktx.provider.ContextProvider
import kotlinx.parcelize.Parcelize

@Parcelize
enum class PreviewType(val stringId: Int): Parcelable {
    IMAGE(R.string.first_photo),
    RANDOM_SOLID_COLOR(R.string.random_solid_color);

    companion object {
        val items by lazy {
            values().map {
                ContextProvider.getContext().getString(it.stringId)
            }.toTypedArray()
        }

        fun valueOf(i: Int): PreviewType {
            return when (i) {
                1 -> RANDOM_SOLID_COLOR
                else -> IMAGE
            }
        }
    }
}
