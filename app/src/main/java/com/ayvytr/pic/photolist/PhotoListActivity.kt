package com.ayvytr.pic.photolist

import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ayvytr.baseadapter.MultiItemTypeAdapter
import com.ayvytr.common.base.BaseListActivity
import com.ayvytr.common.c
import com.ayvytr.flow.base.IView
import com.ayvytr.flow.vm.BaseViewModel
import com.ayvytr.ktx.context.getColorCompat
import com.ayvytr.ktx.ui.getContext
import com.ayvytr.ktx.ui.startActivity
import com.ayvytr.pic.R
import com.ayvytr.pic.bean.Photo
import com.ayvytr.pic.event.FileDeletedEvent
import com.ayvytr.pic.photo.PhotoActivity
import com.biubiu.eventbus.observe.observeEvent
import com.scwang.smart.refresh.layout.api.RefreshLayout

class PhotoListActivity: BaseListActivity<BaseViewModel<IView>, Photo>() {
    val adapter by lazy {
        PhotoListAdapter(getContext())
    }

    lateinit var photoList: ArrayList<Photo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.common_layout_list)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        recyclerView.setBackgroundColor(getColorCompat(R.color.black))

        recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        setAdapter(adapter)

        adapter.onItemClickListener = object: MultiItemTypeAdapter.OnItemClickListener<Photo> {
            override fun onItemClick(holder: RecyclerView.ViewHolder, t: Photo, position: Int) {
                startActivity<PhotoActivity>(c.intent.key_photo to t)
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        photoList = intent.getParcelableArrayListExtra<Photo>(c.intent.key)!!
        autoRefresh()

        observeEvent<FileDeletedEvent> {
            autoRefresh()
        }
    }

    override fun onRefreshCallback(refreshLayout: RefreshLayout) {
        updateData(photoList, 1, false)
    }

}