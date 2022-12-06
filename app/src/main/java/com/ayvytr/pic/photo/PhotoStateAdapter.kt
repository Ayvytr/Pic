package com.ayvytr.pic.photo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ayvytr.pic.bean.Photo

/**
 * @author Administrator
 */
class PhotoStateAdapter(
    activity: FragmentActivity,
    val photoList: MutableList<Photo> = mutableListOf()
):
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun createFragment(position: Int): Fragment {
        return PhotoFragment.newInstance(photoList[position])
    }

}