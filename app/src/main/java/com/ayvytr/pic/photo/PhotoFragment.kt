package com.ayvytr.pic.photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.ayvytr.common.c
import com.ayvytr.common.load
import com.ayvytr.flow.BaseFragment
import com.ayvytr.flow.base.IView
import com.ayvytr.flow.vm.BaseViewModel
import com.ayvytr.pic.bean.Photo
import com.ayvytr.pic.databinding.FragmentPhotoBinding

/**
 * @author Administrator
 */
class PhotoFragment(): BaseFragment<BaseViewModel<IView>>() {
    lateinit var binding: FragmentPhotoBinding

    lateinit var photo: Photo

    companion object {
        fun newInstance(photo: Photo): PhotoFragment {
            val photoFragment = PhotoFragment()
            photoFragment.arguments = bundleOf(c.intent.photo to photo)
            return photoFragment
        }
    }

    override fun getContentViewRes(): Int {
        return -1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPhotoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun initData(savedInstanceState: Bundle?) {
        photo = requireArguments().getParcelable(c.intent.photo)!!
        binding.photoView.load(photo.path)
    }
}