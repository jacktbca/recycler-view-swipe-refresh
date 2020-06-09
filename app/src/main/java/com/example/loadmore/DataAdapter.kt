package com.example.loadmore

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item.view.*

class DataAdapter constructor(
    private val studentList: List<Student?>,
    recyclerView: RecyclerView
) : RecyclerView.Adapter<DataAdapter.StudentViewHolder>() {

    companion object {
        private const val TAG = "DataAdapter"
        private const val THRESHOLD = 3

        private const val VIEW_ITEM = 1
        private const val VIEW_PROGRESS = 0
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    private var isLoading: Boolean = false
    private var onLoadMoreListener: OnLoadMoreListener? = null

    init {
        if (recyclerView.layoutManager is LinearLayoutManager) {
            val layoutManager: LinearLayoutManager =
                recyclerView.layoutManager as LinearLayoutManager
            val totalItemCount = layoutManager.itemCount
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (!isLoading && totalItemCount <= lastPosition + THRESHOLD) {
                        onLoadMoreListener?.run {
                            isLoading = true
                            onLoadMore()
                        }
                    }
                }
            })
        }
    }

    // Call after the loading more is finished
    fun setLoaded() {
        isLoading = false
    }

    fun setOnLoadMoreListener(callback: () -> Unit) {
        onLoadMoreListener = object : OnLoadMoreListener {
            override fun onLoadMore() {
                callback.invoke()
            }
        }
    }

    fun setOnLoadMoreListener(listener: OnLoadMoreListener) {
        onLoadMoreListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return studentList.getOrNull(position)?.run { VIEW_ITEM } ?: VIEW_PROGRESS
    }

    class StudentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.tvName
        val tvEmailId: TextView = view.tvEmailId
        val progressBar: ProgressBar = view.progressBar1

        init {
            view.setOnClickListener {
                // Do nothing
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return StudentViewHolder(view)
    }

    override fun getItemCount(): Int = studentList.size

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList.getOrNull(position)
        if (student != null) {
            holder.tvName.text = student.name
            holder.tvEmailId.text = student.emailId
            holder.progressBar.visibility = View.GONE
        } else {
            holder.progressBar.isIndeterminate = true
            holder.progressBar.visibility = View.VISIBLE
        }
    }
}