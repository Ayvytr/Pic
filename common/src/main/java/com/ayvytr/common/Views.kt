package com.ayvytr.common

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide

/**
 * @author Administrator
 */

fun ImageView.load(
    url: String,
    @DrawableRes errorDrawableId: Int = -1,
    @DrawableRes placeHolderDrawableId: Int = -1
) {
    Glide.with(context)
        .load(url)
        .apply {
            if (errorDrawableId > -1) {
                error(errorDrawableId)
            }
            if (placeHolderDrawableId > -1) {
                placeholder(placeHolderDrawableId)
            }
        }
        .into(this)
}

fun ImageView.loadGif(
    url: String,
    @DrawableRes errorDrawableId: Int = -1,
    @DrawableRes placeHolderDrawableId: Int = -1
) {
    Glide.with(context)
        .asGif()
        .load(url)
        .apply {
            if (errorDrawableId > -1) {
                error(errorDrawableId)
            }
            if (placeHolderDrawableId > -1) {
                placeholder(placeHolderDrawableId)
            }
        }
        .into(this)
}

//fun ImageView.loadAvatar(url: String) {
//    Glide.with(context)
//        .load(url)
//        .placeholder(R.drawable.ic_avatar)
//        .error(R.drawable.ic_avatar)
//        .circleCrop()
//        .into(this)
//}