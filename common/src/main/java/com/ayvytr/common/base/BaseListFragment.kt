package com.ayvytr.common.base

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayvytr.baseadapter.EmptyAdapter
import com.ayvytr.common.R
import com.ayvytr.flow.BaseConfig
import com.ayvytr.flow.BaseFragment
import com.ayvytr.flow.base.IView
import com.ayvytr.flow.vm.BaseViewModel
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

abstract class BaseListFragment<T: BaseViewModel<IView>, B>: BaseFragment<T>() {
    //第一页的页标
    protected var firstPage = 1

    lateinit var recyclerView: RecyclerView
    lateinit var smartRefreshLayout: SmartRefreshLayout

    //当前页的页数
    protected var currentPage = firstPage

    //每页条目数
    protected var pageSize = BaseConfig.PAGE_SIZE

    protected lateinit var emptyAdapter: EmptyAdapter<B>

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
                currentPage = firstPage
                onRefreshCallback(refreshLayout)
            }
        })
    }

    protected open fun findRecyclerView() {
        recyclerView = requireView().findViewById(R.id.recycler_view)
        smartRefreshLayout = requireView().findViewById(R.id.smart_refresh_layout)
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
        if (currentPage == firstPage) {
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
        if (currentPage == firstPage) {
            emptyAdapter.notifyDataSetChanged()
            enableLoadMore(false)
        } else {
            enableLoadMore(true)
        }
        smartRefreshLayout.finishRefresh(false)
        smartRefreshLayout.finishLoadMore(false)
        if (currentPage > firstPage) {
            currentPage--
        }
        smartRefreshLayout.finishRefresh()
    }

    protected fun autoRefresh() {
        smartRefreshLayout.autoRefresh()
    }

    protected fun finishRefreshAndLoadMore() {
        finishRefresh()
        finishLoadMore()
    }

    override fun onDestroyView() {
        finishRefreshAndLoadMore()
        super.onDestroyView()
    }
}