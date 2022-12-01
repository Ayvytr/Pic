package com.ayvytr.common.dialog

import android.content.Context
import android.widget.Button
import android.widget.TextView
import androidx.annotation.StringRes
import com.ayvytr.common.R
import com.ayvytr.ktx.ui.dialog.BaseDialog
import com.ayvytr.ktx.ui.show

/**
 * @author Administrator
 */
class ConfirmDialog(context: Context, val title: CharSequence, val content: CharSequence? = null):
    BaseDialog(context) {

    //    lateinit var tvTitle: TextView
//    lateinit var tvContent: TextView
//    lateinit var btnConfirm: Button
//    lateinit var btnCancel: Button
    val tvTitle: TextView by lazy {
        findViewById(R.id.tv_title)
    }
    val tvContent: TextView by lazy {
        findViewById(R.id.tv_content)
    }
    val btnConfirm: Button by lazy {
        findViewById(R.id.btn_confirm)
    }
    val btnCancel: Button by lazy {
        findViewById(R.id.btn_cancel)
    }

    private var confirm: () -> Unit = {}
    private var cancel: () -> Unit = {}

    private var isShowCancel = true

    constructor(context: Context, @StringRes titleRes: Int, @StringRes contentRes: Int = -1): this(
        context,
        context.getString(titleRes),
        if (contentRes > -1) context.getString(contentRes) else null
    )

    override fun getContentView(): Int {
        return R.layout.dialog_confirm
    }

    override fun initView() {
//        tvTitle = findViewById(R.id.tv_title)
//        tvContent = findViewById(R.id.tv_content)
//        btnConfirm = findViewById(R.id.btn_confirm)
//        btnCancel = findViewById(R.id.btn_cancel)

        btnConfirm.setOnClickListener {
            confirm.invoke()
            dismiss()
        }
        btnCancel.setOnClickListener {
            cancel.invoke()
            dismiss()
        }
        btnCancel.show(isShowCancel)

        tvTitle.text = title
        content?.let {
            tvContent.text = content
            tvContent.show()
        }
    }

    fun showCancel(isShow: Boolean = true): ConfirmDialog {
        isShowCancel = isShow
        return this
    }

    fun noCancelableOutside(): ConfirmDialog {
        setCanceledOnTouchOutside(false)
        return this
    }

    fun onConfirm(o: () -> Unit): ConfirmDialog {
        confirm = o
        return this
    }

    fun onCancel(c: () -> Unit): ConfirmDialog {
        cancel = c
        return this
    }

}