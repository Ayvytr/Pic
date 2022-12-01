package com.ayvytr.common.base

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayvytr.baseadapter.EmptyAdapter
import com.ayvytr.common.R
import com.ayvytr.flow.BaseActivity
import com.ayvytr.flow.BaseConfig
import com.ayvytr.flow.base.IView
import com.ayvytr.flow.vm.BaseViewModel
import com.ayvytr.ktx.ui.getContext
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

/**
 * 列表展示的基类
 */
abstract class BaseListActivity<T: BaseViewModel<IView>, B>: BaseActivity<T>() {
    //第一页的页标
    protected var FIRST_PAGE = 1

    lateinit var recyclerView: RecyclerView
    lateinit var smartRefreshLayout: SmartRefreshLayout

    //当前页的页数
    protected var currentPage = FIRST_PAGE

    //每页条目数
    protected var pageSize = BaseConfig.PAGE_SIZE

    protected lateinit var emptyAdapter: EmptyAdapter<B>

    @CallSuper
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        findRecyclerView()

        recyclerView.layoutManager = LinearLayoutManager(getContext())
        smartRefreshLayout.setOnRefreshLoadMoreListener(object: OnRefreshLoadMoreListener {
            override fun onLoadMore(refreshLayout: RefreshLayout) {
                currentPage++
                onLoadMoreCallback(refreshLayout)
            }

            override fun onRefresh(refreshLayout: RefreshLayout) {
                currentPage = FIRST_PAGE
                onRefreshCallback(refreshLayout)
            }
        })
    }

    protected open fun findRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view)
        smartRefreshLayout = findViewById(R.id.smart_refresh_layout)
    }

    protected abstract fun onRefreshCallback(refreshLayout: RefreshLayout)

    protected open fun onLoadMoreCallback(refreshLayout: RefreshLayout) {

    }

    protected fun enableRefresh(enable: Boolean) {
        smartRefreshLayout.setEnableRefresh(enable)
    }

    protected fun enableLoadMore(enable: Boolean) {
        smartRefreshLayout.setEnableLoadMore(enable)
    }

    protected fun finishRefresh() {
        smartRefreshLayout.finishRefresh()
    }

    protected fun finishLoadMore() {
        smartRefreshLayout.finishLoadMore()
    }

    protected fun setAdapter(adapter: EmptyAdapter<B>) {
        this.emptyAdapter = adapter
        recyclerView.adapter = this.emptyAdapter
    }

    /**
     * 更新数据，page==totalPage时，禁用上拉加载.
     */
    protected fun updateData(list: List<B>, page: Int, totalPage: Int) {
        updateData(list, page, totalPage > page)
    }

    /**
     * 更新数据，haveMoreData=false时，禁用上拉加载.
     */
    protected fun updateData(list: List<B>, page: Int, haveMoreData: Boolean) {
        currentPage = page
        if (currentPage == FIRST_PAGE) {
            emptyAdapter.updateList(list)
        } else {
            emptyAdapter.add(list)
        }
        finishRefreshAndLoadMore()
        enableLoadMore(haveMoreData)
    }

    /**
     * 更新数据失败，停止刷新
     */
    @SuppressLint("NotifyDataSetChanged")
    protected fun updateDataFailed() {
        if (currentPage == FIRST_PAGE) {
            emptyAdapter.notifyDataSetChanged()
            enableLoadMore(false)
        } else {
            enableLoadMore(true)
        }
        smartRefreshLayout.finishRefresh(false)
        smartRefreshLayout.finishLoadMore(false)
        if (currentPage > FIRST_PAGE) {
            currentPage--
        }
    }

    protected fun autoRefresh() {
        smartRefreshLayout.autoRefresh()
    }

    protected fun finishRefreshAndLoadMore() {
        finishRefresh()
        finishLoadMore()
    }

    override fun onDestroy() {
        finishRefreshAndLoadMore()
        super.onDestroy()
    }
}