package com.ayvytr.pic.main

import android.content.Context
import android.widget.ImageView
import com.ayvytr.baseadapter.EmptyAdapter
import com.ayvytr.baseadapter.ViewHolder
import com.ayvytr.common.load
import com.ayvytr.ktx.context.getScreenWidth
import com.ayvytr.pic.R
import com.ayvytr.pic.bean.Path

/**
 * @author Administrator
 */
class MainAdapter(context: Context):
    EmptyAdapter<Path>(context, R.layout.item_preview, R.layout.item_main_empty) {

    val photoHeight = context.getScreenWidth() / 2

    override fun onBindView(holder: ViewHolder, t: Path, position: Int, payloads: List<Any>) {
        holder.getView<ImageView>(R.id.iv).load(t.previewPhoto)

        holder.setText(R.id.tv_name, t.name)
        holder.setText(R.id.tv_count, t.count.toString())

        holder.itemView.layoutParams.height = photoHeight
    }

    override fun onBindEmptyView(holder: ViewHolder) {
    }
}