package com.ayvytr.pic.photo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.ayvytr.common.c
import com.ayvytr.common.dialog.ConfirmDialog
import com.ayvytr.flow.BaseActivity
import com.ayvytr.flow.base.IView
import com.ayvytr.flow.vm.BaseViewModel
import com.ayvytr.ktx.ui.getContext
import com.ayvytr.pic.R
import com.ayvytr.pic.bean.Photo
import com.ayvytr.pic.databinding.ActivityPhotoBinding
import com.ayvytr.pic.event.FileDeletedEvent
import com.biubiu.eventbus.post.postEvent
import com.bumptech.glide.Glide
import java.io.File

class PhotoActivity: BaseActivity<BaseViewModel<IView>>() {
    lateinit var binding: ActivityPhotoBinding
    lateinit var photo: Photo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(getLayoutInflater())
        setContentView(binding.root)
    }

    override fun initView(savedInstanceState: Bundle?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        photo = intent.getParcelableExtra<Photo>(c.intent.key_photo)!!
        Glide.with(this)
            .load(photo.path)
            .into(binding.photoView)
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
        val file = File(photo.path)
        if (file.delete()) {
            postEvent(FileDeletedEvent())
            finish()
        } else {
            showMessage(R.string.photo_delete_failed)
        }
    }

    private fun showPhotoDetailDialog() {
        val text = "${photo.name}\n${photo.path}\n${photo.size}\n" +
                "${photo.width}×${photo.height}\n "
        ConfirmDialog(getContext(), text).showCancel(false).show()
    }
}