package com.ayvytr.pic.main

import android.Manifest
import android.app.AlertDialog
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayvytr.baseadapter.MultiItemTypeAdapter
import com.ayvytr.common.MmkvManager
import com.ayvytr.common.base.BaseListFragment
import com.ayvytr.common.bean.PreviewType
import com.ayvytr.common.bean.SortType
import com.ayvytr.common.bean.Storage
import com.ayvytr.common.c
import com.ayvytr.flow.base.IView
import com.ayvytr.flow.vm.BaseViewModel
import com.ayvytr.ktx.context.getColorCompat
import com.ayvytr.ktx.ui.startActivity
import com.ayvytr.logger.L
import com.ayvytr.pic.R
import com.ayvytr.pic.bean.Path
import com.ayvytr.pic.bean.Photo
import com.ayvytr.pic.event.PhotoDeletedEvent
import com.ayvytr.pic.event.PreviewTypeChangedEvent
import com.ayvytr.pic.event.SortTypeChangedEvent
import com.ayvytr.pic.photolist.PhotoListActivity
import com.biubiu.eventbus.observe.observeEvent
import com.biubiu.eventbus.post.postEvent
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.scwang.smart.refresh.layout.api.RefreshLayout
import kotlinx.coroutines.launch
import java.io.File
import kotlin.random.Random

/**
 * @author Administrator
 */
class MainFragment: BaseListFragment<BaseViewModel<IView>, Path>() {

    val imageUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val imageProjection = arrayOf(
        MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.HEIGHT,
        MediaStore.Images.Media.WIDTH,
        MediaStore.Images.Media.DATE_ADDED,
        MediaStore.Images.Media.DATE_MODIFIED
    )

    val photoMap = mutableMapOf<String, MutableList<Photo>>()
    val photoPathList = mutableListOf<Path>()

    val random = Random(255)

    val adapter by lazy {
        MainAdapter(requireContext())
    }

    override fun getContentViewRes(): Int {
        return R.layout.common_layout_list
    }


    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        recyclerView.setBackgroundColor(getColorCompat(R.color.black))

//        recyclerView.layoutManager =
//            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        recyclerView.layoutManager =
            GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false)

        setAdapter(adapter)

        adapter.onItemClickListener = object: MultiItemTypeAdapter.OnItemClickListener<Path> {
            override fun onItemClick(holder: RecyclerView.ViewHolder, t: Path, position: Int) {
                startActivity<PhotoListActivity>(c.intent.photo_list to photoMap[t.path])
            }
        }

    }

    override fun initData(savedInstanceState: Bundle?) {
        if (!XXPermissions.isGranted(getContext(), Manifest.permission.MANAGE_EXTERNAL_STORAGE)) {
            showMessage("没有存权限")

            XXPermissions.with(this)
                .permission(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                .request(object: OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                        autoRefresh()
                    }

                    override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                        showMessage(R.string.cannot_get_storage_permission)
                    }
                })
        } else {
            autoRefresh()
        }

        observeEvent<PhotoDeletedEvent> {
            autoRefresh()
        }

        observeEvent<SortTypeChangedEvent> {
//            autoRefresh()
            makePathList()
        }
    }

    private fun queryPhoto() {
        viewModel.launch {

            val cursor = requireContext().contentResolver.query(
                imageUri,
                imageProjection,
//            MediaStore.Images.Media.MIME_TYPE + " in(?)", arrayOf("image/*"),
                null,
//            MediaStore.Images.Media.DATE_MODIFIED + " desc"
                null,
                null
            )
            if (cursor == null) {
                showMessage("cursor is null!")
                return@launch
            }

            photoMap.clear()

            L.e(cursor.count, cursor.columnNames, cursor.extras)

            val pathIndex: Int = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
            L.e(pathIndex, cursor.getColumnName(pathIndex))
            if (cursor.moveToFirst()) {
                do {
                    // 获取图片的路径
                    val filePath: String = cursor.getString(pathIndex)
//                    L.e(filePath)
                    val parentFile = File(filePath).parentFile ?: continue

                    //获取的文件目录
                    val path = parentFile.absolutePath
//                    L.e(path)


                    val nameIndex = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)
                    val name = cursor.getString(nameIndex)

//                    L.e(filePath, name)

                    val sizeIndex = cursor.getColumnIndex(MediaStore.Images.Media.SIZE)
                    val size = cursor.getInt(sizeIndex)

                    val widthIndex = cursor.getColumnIndex(MediaStore.Images.Media.WIDTH)
                    val width = cursor.getInt(widthIndex)

                    val heightIndex = cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT)
                    val height = cursor.getInt(heightIndex)

                    val addedIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
                    val addedSecond = cursor.getInt(addedIndex)

                    val modifiedIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED)
                    val modifiedSecond = cursor.getInt(modifiedIndex)

                    val photo = Photo(
                        name, path, filePath, Storage(size), width, height, addedSecond * 1000L,
                        modifiedSecond * 1000L
                    )

                    val containsPath = photoMap.containsKey(path)
                    if (containsPath) {
                        photoMap.get(path)!!.add(photo)
                    } else {
                        photoMap.put(path, mutableListOf(photo))
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()


//            L.e(photoMap.size, photoMap.keys)
//            for ((key, value) in photoMap) {
//                L.e(key, value.size)
//            }

            makePathList()
        }

    }

    private fun makePathList() {
        L.e(MmkvManager.getSortType())
        photoPathList.clear()
        for ((key, list) in photoMap) {
            when (MmkvManager.getSortType()) {
                SortType.TIME_DESC -> list.sortByDescending { it.modifiedDate }
                SortType.TIME -> list.sortBy { it.modifiedDate }
                else -> list.sortBy { it.name }
            }

            val photo = list.get(0)

            val color = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255))
            photoPathList.add(
                Path(
                    photo.parent.substring(photo.parent.lastIndexOf('/') + 1), key, list.size,
                    photo.path, color
                )
            )
        }
        photoPathList.sortBy { it.name }

        L.e(photoPathList.size)
        requireActivity().runOnUiThread {
            showPathList()
        }
    }

    private fun showPathList() {
        updateData(photoPathList, 1, 1)
//        adapter.updateList(photoPathList)
//        smart_refresh_layout.finishRefresh()
    }

    override fun onRefreshCallback(refreshLayout: RefreshLayout) {
        queryPhoto()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mid_preview_type) {
            selectPreviewType()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun selectPreviewType() {
        AlertDialog.Builder(getContext())
            .setTitle(R.string.preview_type)
            .setSingleChoiceItems(
                PreviewType.items, MmkvManager.getPreviewType().ordinal
            ) { dialog, which ->
                val origin = MmkvManager.getPreviewType()
                val previewType = PreviewType.valueOf(which)
                if (origin != previewType) {
                    MmkvManager.setPreviewType(previewType)
                    postEvent(PreviewTypeChangedEvent())
                }
                showPathList()
                dialog.dismiss()
            }
            .show()
    }
}