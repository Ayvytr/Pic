package com.ayvytr.pic.main

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import com.ayvytr.baseadapter.EmptyAdapter
import com.ayvytr.baseadapter.ViewHolder
import com.ayvytr.common.MmkvManager
import com.ayvytr.common.bean.PreviewType
import com.ayvytr.common.load
import com.ayvytr.ktx.context.getScreenWidth
import com.ayvytr.pic.R
import com.ayvytr.pic.bean.Path
import com.bumptech.glide.Glide

/**
 * @author Administrator
 */
class MainAdapter(context: Context):
    EmptyAdapter<Path>(context, R.layout.item_preview, R.layout.item_main_empty) {

    val photoHeight = context.getScreenWidth() / 2

    override fun onBindView(holder: ViewHolder, t: Path, position: Int, payloads: List<Any>) {
        val iv = holder.getView<ImageView>(R.id.iv)
        if(MmkvManager.getPreviewType() == PreviewType.IMAGE) {
            Glide.with(context)
                .load(t.previewPhoto)
                .centerCrop()
                .into(iv)
        } else {
            iv.setImageDrawable(ColorDrawable(t.previewColor))
        }

        holder.setText(R.id.tv_name, t.name)
        holder.setText(R.id.tv_count, t.count.toString())

        holder.itemView.layoutParams.height = photoHeight
    }

    override fun onBindEmptyView(holder: ViewHolder) {
    }
}