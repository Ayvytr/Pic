package com.ayvytr.pic.main

import android.Manifest
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayvytr.baseadapter.MultiItemTypeAdapter
import com.ayvytr.common.base.BaseListActivity
import com.ayvytr.common.bean.Storage
import com.ayvytr.common.c
import com.ayvytr.ktx.ui.getContext
import com.ayvytr.ktx.ui.startActivity
import com.ayvytr.logger.L
import com.ayvytr.pic.R
import com.ayvytr.pic.bean.Path
import com.ayvytr.pic.bean.Photo
import com.ayvytr.pic.photolist.PhotoListActivity
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.scwang.smart.refresh.layout.api.RefreshLayout
import kotlinx.coroutines.launch
import java.io.File
import kotlin.random.Random


class MainActivity: BaseListActivity<MainViewModel, Path>() {

    val imageUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val imageProjection = arrayOf(
        MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.HEIGHT,
        MediaStore.Images.Media.WIDTH
    )

    val photoMap = mutableMapOf<String, MutableList<Photo>>()
    val photoPathList = mutableListOf<Path>()

    val random = Random(255)

    val adapter by lazy {
        MainAdapter(getContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.common_layout_list)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
//        recyclerView.layoutManager =
//            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        recyclerView.layoutManager =
            GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false)

        setAdapter(adapter)

        adapter.onItemClickListener = object: MultiItemTypeAdapter.OnItemClickListener<Path> {
            override fun onItemClick(holder: RecyclerView.ViewHolder, t: Path, position: Int) {
                startActivity<PhotoListActivity>(c.intent.key to photoMap[t.path])
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
                        smartRefreshLayout.autoRefresh()
                    }

                    override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                        showMessage(R.string.cannot_get_storage_permission)
                    }
                })
        } else {
            smartRefreshLayout.autoRefresh()
        }
    }

    //    private fun scanPhoto() {
//        GlobalScope.launch {
//            val root = Environment.getExternalStorageDirectory()
////            scanDir(root)
//            scanDir(File(root, "0"))
//        }
//    }
//
//    private fun scanDir(root: File) {
//        val files = root.listFiles()
//        if (files == null) {
//            L.e(root)
//            return
//        }
//
//        for (file in files) {
//            if (file.isDirectory) {
//                scanDir(file)
//            } else {
//                val type = contentResolver.getType(file.toUri())
//                L.e(file, type)
//            }
//        }
//    }

    private fun queryPhoto() {
        viewModel.launch {

            val cursor = contentResolver.query(
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

                    val photo = Photo(name, path, filePath, Storage(size), width, height)

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
        photoPathList.clear()
        for ((key, list) in photoMap) {
            val photo = list.get(0)

            val color = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255))
            photoPathList.add(
                Path(
                    photo.parent.substring(photo.parent.lastIndexOf('/') + 1), key, list.size,
                    photo.path, color
                )
            )
        }

        L.e(photoPathList.size)
        runOnUiThread {
            showPathList()
        }
    }

    private fun showPathList() {
        L.e(Thread.currentThread().name)
        updateData(photoPathList, 1, 1)
//        adapter.updateList(photoPathList)
//        smart_refresh_layout.finishRefresh()
    }

    override fun onRefreshCallback(refreshLayout: RefreshLayout) {
        queryPhoto()
    }
}