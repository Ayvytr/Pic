package com.ayvytr.pic.photolist

import android.content.Context
import android.widget.ImageView
import com.ayvytr.baseadapter.EmptyAdapter
import com.ayvytr.baseadapter.ViewHolder
import com.ayvytr.common.MmkvManager
import com.ayvytr.common.bean.PreviewType
import com.ayvytr.common.load
import com.ayvytr.ktx.context.getScreenWidth
import com.ayvytr.pic.R
import com.ayvytr.pic.bean.Photo
import com.bumptech.glide.Glide

/**
 * @author Administrator
 */
class PhotoListAdapter(context: Context):
    EmptyAdapter<Photo>(context, R.layout.item_photo, R.layout.item_main_empty) {

    val photoHeight = context.getScreenWidth() / 2

    override fun onBindView(holder: ViewHolder, t: Photo, position: Int, payloads: List<Any>) {
        val iv = holder.getView<ImageView>(R.id.iv)
        Glide.with(context)
            .load(t.path)
            .centerCrop()
            .into(iv)

        holder.itemView.layoutParams.height = photoHeight
    }

    override fun onBindEmptyView(holder: ViewHolder) {
    }
}