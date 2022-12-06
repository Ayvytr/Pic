package com.ayvytr.pic.photo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.ayvytr.common.c
import com.ayvytr.common.dialog.ConfirmDialog
import com.ayvytr.flow.BaseActivity
import com.ayvytr.flow.base.IView
import com.ayvytr.flow.vm.BaseViewModel
import com.ayvytr.ktx.ui.getContext
import com.ayvytr.pic.R
import com.ayvytr.pic.bean.Photo
import com.ayvytr.pic.databinding.ActivityPhotoBinding
import com.ayvytr.pic.event.PhotoDeletedEvent
import com.biubiu.eventbus.post.postEvent
import java.io.File

class PhotoActivity: BaseActivity<BaseViewModel<IView>>() {
    lateinit var binding: ActivityPhotoBinding
    var index = 0

    val adapter = PhotoStateAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(getLayoutInflater())
        setContentView(binding.root)
    }

    override fun initView(savedInstanceState: Bundle?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.vp.adapter = adapter
        binding.vp.registerOnPageChangeCallback(object: OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                index = position
            }
        })
    }

    override fun initData(savedInstanceState: Bundle?) {
        val photoList = intent.getParcelableArrayListExtra<Photo>(c.intent.photo_list)!!
        index = intent.getIntExtra(c.intent.key_index, 0)

        adapter.photoList.addAll(photoList)
        adapter.notifyDataSetChanged()

        binding.vp.currentItem = index
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_photo, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mid_detail -> showPhotoDetailDialog()
            R.id.mid_delete -> askDeletePhoto()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun askDeletePhoto() {
        ConfirmDialog(getContext(), R.string.sure_delete_photo).onConfirm {
            performDeletePhoto()
        }.noCancelableOutside().show()
    }

    private fun performDeletePhoto() {
        val photo = adapter.photoList[index]
//        val file = File(photo.path)
//        if (file.delete()) {
//            postEvent(PhotoDeletedEvent(photo))
//            sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)))
//
//            adapter.photoList.removeAt(index)
//            adapter.notifyItemRemoved(index)
//        } else {
//            showMessage(R.string.photo_delete_failed)
//        }
        adapter.photoList.removeAt(index)
        adapter.notifyItemRemoved(index)

    }

    private fun showPhotoDetailDialog() {
        val photo = adapter.photoList[index]
        val text = "${photo.name}\n${photo.path}\n${photo.size}\n" +
                "${photo.width}×${photo.height}\n" +
                photo.modifiedDateStr()

        ConfirmDialog(getContext(), text).showCancel(false).show()
    }
}