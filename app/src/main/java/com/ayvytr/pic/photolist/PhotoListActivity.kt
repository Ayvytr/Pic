package com.ayvytr.pic.photolist

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ayvytr.baseadapter.MultiItemTypeAdapter
import com.ayvytr.common.MmkvManager
import com.ayvytr.common.base.BaseListActivity
import com.ayvytr.common.bean.SortType
import com.ayvytr.common.c
import com.ayvytr.flow.base.IView
import com.ayvytr.flow.vm.BaseViewModel
import com.ayvytr.ktx.context.getColorCompat
import com.ayvytr.ktx.ui.getContext
import com.ayvytr.ktx.ui.startActivity
import com.ayvytr.pic.R
import com.ayvytr.pic.bean.Photo
import com.ayvytr.pic.event.PhotoDeletedEvent
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
                startActivity<PhotoActivity>(
                    c.intent.photo_list to photoList, c.intent.key_index to position
                )
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        photoList = intent.getParcelableArrayListExtra<Photo>(c.intent.photo_list)!!
        autoRefresh()

        observeEvent<PhotoDeletedEvent> { event ->
            photoList.removeIf { it.path == event.photo.path }
            updateData(photoList, 1, false)
        }
//        observeEvent<SortTypeChangedEvent> {
//            when (MmkvManager.getSortType()) {
//                SortType.TIME_DESC -> photoList.sortByDescending { it.modifiedDate }
//                SortType.TIME -> photoList.sortBy { it.modifiedDate }
//                else -> photoList.sortBy { it.name }
//            }
//            updateData(photoList, 1, false)
//        }
    }

    override fun onRefreshCallback(refreshLayout: RefreshLayout) {
        sortPhotoList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_photo_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mid_sort_type) {
            selectSortType()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun selectSortType() {
        AlertDialog.Builder(getContext())
            .setTitle(R.string.sort_by)
            .setSingleChoiceItems(
                SortType.items, MmkvManager.getSortType().ordinal
            ) { dialog, which ->
                val origin = MmkvManager.getSortType()
                val sortType = SortType.valueOf(which)
                if (origin != sortType) {
                    MmkvManager.setSortType(sortType)
                }
                sortPhotoList()
                dialog.dismiss()
            }
            .show()
    }

    private fun sortPhotoList() {
        val list = photoList
        when (MmkvManager.getSortType()) {
            SortType.TIME_DESC -> list.sortByDescending { it.modifiedDate }
            SortType.TIME -> list.sortBy { it.modifiedDate }
            else -> list.sortBy { it.name }
        }
        updateData(photoList, 1, false)
    }
}